const API_HERRAMIENTAS = "/herramientas";
const API_ALQUILERES = "/alquileres";

let herramientas = [];
let alquileres = [];
let filtroActual = "todos";

const $ = (id) => document.getElementById(id);

document.addEventListener("DOMContentLoaded", () => {
    $("formHerramienta").addEventListener("submit", registrarHerramienta);
    $("formAlquiler").addEventListener("submit", registrarAlquiler);
    $("btnActualizar").addEventListener("click", cargarDatos);

    $("btnTodos").addEventListener("click", () => cambiarFiltro("todos"));
    $("btnActivos").addEventListener("click", () => cambiarFiltro("activos"));
    $("btnAtrasados").addEventListener("click", () => cambiarFiltro("atrasados"));

    $("fechaAlquiler").value = fechaLocalActual();
    $("fechaPrevistaDevolucion").min = fechaLocalActual();

    cargarDatos();
});

function fechaLocalActual() {
    const ahora = new Date();
    const zonaLocal = new Date(ahora.getTime() - ahora.getTimezoneOffset() * 60000);
    return zonaLocal.toISOString().slice(0, 10);
}

async function peticion(url, opciones = {}) {
    const respuesta = await fetch(url, {
        ...opciones,
        headers: {
            ...(opciones.body ? { "Content-Type": "application/json" } : {}),
            ...opciones.headers
        }
    });

    const texto = await respuesta.text();
    let datos = null;

    if (texto) {
        try {
            datos = JSON.parse(texto);
        } catch {
            datos = texto;
        }
    }

    if (!respuesta.ok) {
        let mensaje = `Error HTTP ${respuesta.status}`;

        if (typeof datos === "string") {
            mensaje = datos;
        } else if (datos) {
            mensaje = datos.message || datos.mensaje || datos.error || mensaje;
        }

        throw new Error(mensaje);
    }

    return datos;
}

function mostrarMensaje(texto, error = false) {
    const elemento = $("mensaje");
    elemento.textContent = texto;
    elemento.className = error ? "mensaje-error" : "mensaje-exito";
}

async function cargarDatos() {
    mostrarMensaje("Actualizando información...");

    try {
        const resultados = await Promise.allSettled([
            peticion(API_HERRAMIENTAS),
            peticion(API_ALQUILERES),
            peticion(`${API_ALQUILERES}/resumen`)
]);

const [resultadoHerramientas, resultadoAlquileres, resultadoResumen] = resultados;

if (resultadoHerramientas.status === "fulfilled") {
    herramientas = Array.isArray(resultadoHerramientas.value)
        ? resultadoHerramientas.value : [];
    renderizarHerramientas();
    renderizarSelectorHerramientas();
} else {
    $("tablaHerramientas").innerHTML =
        `<tr><td colspan="5">No se pudieron cargar las herramientas.</td></tr>`;
    $("herramientaId").innerHTML =
        `<option value="">Error al cargar herramientas</option>`;
}

if (resultadoAlquileres.status === "fulfilled") {
    alquileres = Array.isArray(resultadoAlquileres.value)
        ? resultadoAlquileres.value : [];
    renderizarAlquileres();
} else {
    $("tablaAlquileres").innerHTML =
        `<tr><td colspan="8">No se pudieron cargar los alquileres.</td></tr>`;
}

if (resultadoResumen.status === "fulfilled") {
    renderizarResumen(resultadoResumen.value);
} else {
    renderizarResumenLocal();
}

const errores = resultados.filter(r => r.status === "rejected");

if (errores.length) {
    mostrarMensaje(
        "Algunos datos no se pudieron cargar. Revisá las direcciones de la API.",
        true
    );
    console.error("Errores al cargar datos:", errores);
} else {
    mostrarMensaje("Datos actualizados correctamente.");
}
} catch (error) {
    console.error(error);
    mostrarMensaje(`No se pudieron cargar los datos: ${error.message}`, true);
}
}

function renderizarHerramientas() {
    const cuerpo = $("tablaHerramientas");

    if (!herramientas.length) {
        cuerpo.innerHTML = `<tr><td colspan="5">No hay herramientas registradas.</td></tr>`;
        return;
    }

    cuerpo.innerHTML = herramientas.map(h => `
        <tr>
            <td>${escapar(h.id)}</td>
            <td>${escapar(h.nombre)}</td>
            <td>${escapar(h.descripcion || "")}</td>
            <td>${moneda(h.tarifaDiaria)}</td>
            <td>${h.disponible ? "Disponible" : "No disponible"}</td>
        </tr>
    `).join("");
}

function renderizarSelectorHerramientas() {
    const selector = $("herramientaId");
    const disponibles = herramientas.filter(h => h.disponible === true);

    if (!disponibles.length) {
        selector.innerHTML = `<option value="">No hay herramientas disponibles</option>`;
        return;
    }

    selector.innerHTML = `
        <option value="">Seleccioná una herramienta</option>
        ${disponibles.map(h => `
            <option value="${escapar(h.id)}">
                ${escapar(h.nombre)} — ${moneda(h.tarifaDiaria)} por día
            </option>
        `).join("")}
    `;
}

function renderizarAlquileres() {
    const cuerpo = $("tablaAlquileres");

    let lista = [...alquileres];

    if (filtroActual === "activos") {
        lista = lista.filter(a => !a.fechaDevolucion);
    } else if (filtroActual === "atrasados") {
        const hoy = fechaLocalActual();
        lista = lista.filter(a =>
            !a.fechaDevolucion && a.fechaPrevistaDevolucion < hoy
        );
    }

    if (!lista.length) {
        cuerpo.innerHTML = `<tr><td colspan="8">No hay alquileres para mostrar.</td></tr>`;
        return;
    }

    cuerpo.innerHTML = lista.map(a => {
        const herramienta = a.herramienta?.nombre || "Sin herramienta";
        const activo = !a.fechaDevolucion;
        const atrasado = activo && a.fechaPrevistaDevolucion < fechaLocalActual();

        let accion = "Devuelto";

        if (activo) {
            accion = `
                <button type="button" data-devolver="${escapar(a.id)}">
                    Registrar devolución
                </button>
            `;
        }

        return `
            <tr>
                <td>${escapar(a.id)}</td>
                <td>${escapar(a.nombreCliente)}</td>
                <td>${escapar(herramienta)}</td>
                <td>${escapar(a.fechaAlquiler)}</td>
                <td>${escapar(a.fechaPrevistaDevolucion)}</td>
                <td>${escapar(a.fechaDevolucion || "Pendiente")}</td>
                <td>${moneda(a.costoEstimado)}</td>
                <td>
                    ${atrasado ? "Atrasado · " : ""}
                    ${accion}
                </td>
            </tr>
        `;
    }).join("");

    cuerpo.querySelectorAll("[data-devolver]").forEach(boton => {
        boton.addEventListener("click", () =>
            registrarDevolucion(boton.dataset.devolver)
        );
    });
}

function renderizarResumen(resumen) {
    $("totalAlquileres").textContent = resumen.totalAlquileres ?? 0;
    $("alquileresActivos").textContent = resumen.alquileresActivos ?? 0;
    $("alquileresAtrasados").textContent = resumen.alquileresAtrasados ?? 0;
    $("costoEstimadoTotal").textContent = moneda(resumen.costoEstimadoTotal);
}

function renderizarResumenLocal() {
    const hoy = fechaLocalActual();
    const activos = alquileres.filter(a => !a.fechaDevolucion);
    const atrasados = activos.filter(a => a.fechaPrevistaDevolucion < hoy);
    const total = alquileres.reduce((suma, a) => suma + Number(a.costoEstimado || 0), 0);

    renderizarResumen({
        totalAlquileres: alquileres.length,
        alquileresActivos: activos.length,
        alquileresAtrasados: atrasados.length,
        costoEstimadoTotal: total
    });
}

async function registrarHerramienta(evento) {
    evento.preventDefault();

    const formulario = evento.currentTarget;
    const datos = new FormData(formulario);

    const herramienta = {
        nombre: String(datos.get("nombre") || "").trim(),
        descripcion: String(datos.get("descripcion") || "").trim(),
        tarifaDiaria: Number(datos.get("tarifaDiaria")),
        disponible: datos.get("disponible") === "true"
    };

    if (!herramienta.nombre || !Number.isFinite(herramienta.tarifaDiaria) ||
        herramienta.tarifaDiaria <= 0) {
        mostrarMensaje("Ingresá un nombre y una tarifa diaria válida.", true);
        return;
    }

    try {
        await peticion(API_HERRAMIENTAS, {
            method: "POST",
            body: JSON.stringify(herramienta)
        });

        formulario.reset();
        mostrarMensaje("Herramienta registrada correctamente.");
        await cargarDatos();
    } catch (error) {
        console.error(error);
        mostrarMensaje(`No se pudo registrar la herramienta: ${error.message}`, true);
    }
}

async function registrarAlquiler(evento) {
    evento.preventDefault();

    const formulario = evento.currentTarget;
    const datos = new FormData(formulario);

    const alquiler = {
        nombreCliente: String(datos.get("nombreCliente") || "").trim(),
        herramientaId: Number(datos.get("herramientaId")),
        fechaAlquiler: datos.get("fechaAlquiler"),
        fechaPrevistaDevolucion: datos.get("fechaPrevistaDevolucion")
    };

    if (!alquiler.nombreCliente || !alquiler.herramientaId ||
        !alquiler.fechaAlquiler || !alquiler.fechaPrevistaDevolucion) {
        mostrarMensaje("Completá todos los campos del alquiler.", true);
        return;
    }

    if (alquiler.fechaPrevistaDevolucion <= alquiler.fechaAlquiler) {
        mostrarMensaje(
            "La devolución prevista debe ser posterior a la fecha de alquiler.",
            true
        );
        return;
    }

    try {
        await peticion(API_ALQUILERES, {
            method: "POST",
            body: JSON.stringify(alquiler)
        });

        formulario.reset();
        $("fechaAlquiler").value = fechaLocalActual();
        mostrarMensaje("Alquiler registrado correctamente.");
        await cargarDatos();
    } catch (error) {
        console.error(error);
        mostrarMensaje(`No se pudo registrar el alquiler: ${error.message}`, true);
    }
}

async function registrarDevolucion(id) {
    if (!confirm("¿Confirmás la devolución de esta herramienta?")) return;

    try {
        await peticion(`${API_ALQUILERES}/${encodeURIComponent(id)}/devolucion`, {
            method: "PUT"
        });

        mostrarMensaje("Devolución registrada correctamente.");
        await cargarDatos();
    } catch (error) {
        console.error(error);
        mostrarMensaje(`No se pudo registrar la devolución: ${error.message}`, true);
    }
}

function cambiarFiltro(filtro) {
    filtroActual = filtro;
    renderizarAlquileres();
}

function moneda(valor) {
    const numero = Number(valor || 0);
    return Number.isFinite(numero)
        ? `Q${numero.toFixed(2)}`
        : "Q0.00";
}

function escapar(valor) {
    return String(valor ?? "").replace(/[&<>"']/g, caracter => ({
        "&": "&amp;",
        "<": "&lt;",
        ">": "&gt;",
        '"': "&quot;",
        "'": "&#39;"
    })[caracter]);
}