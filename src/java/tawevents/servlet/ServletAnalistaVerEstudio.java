package tawevents.servlet;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tawevents.dao.EstudioFacade;
import tawevents.dao.EtiquetaFacade;
import tawevents.dao.EventoFacade;
import tawevents.dao.UsuarioDeEventosFacade;
import tawevents.entity.Estudio;
import tawevents.entity.Evento;
import tawevents.entity.Publico;
import tawevents.entity.Usuario;
import tawevents.entity.UsuarioDeEventos;

/**
 *
 * @author daniel
 */
@WebServlet(name = "ServletAnalistaVerEstudio", urlPatterns = {"/ServletAnalistaVerEstudio"})
public class ServletAnalistaVerEstudio extends HttpServlet {

    @EJB
    private EventoFacade eventoFacade;

    @EJB
    private UsuarioDeEventosFacade usuarioDeEventosFacade;

    @EJB
    private EstudioFacade estudioFacade;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String modo = null;
        Estudio estudio = null;
        List<String> datos = null;
        Set<String> ciudades = null;

        modo = request.getParameter("modo");

        String strID = request.getParameter("id");
        if (strID != null) {
            int id;
            id = Integer.parseInt(strID);
            estudio = this.estudioFacade.find(id);
            datos = separarDatos(estudio.getBusqueda());
        } else {
            datos = datosVacios();
        }

        if (modo.equals("crear")) {
            ciudades = this.estudioFacade.findAllCiudades();
            request.setAttribute("ciudades", ciudades);
        } else if (modo.equals("ver")) {
            List<Usuario> resultados = analizarBaseDeDatos(datos);
            request.setAttribute("resultados", resultados);
        }
        request.setAttribute("estudio", estudio);
        request.setAttribute("datos", datos);
        request.setAttribute("modo", modo);

        RequestDispatcher rd = request.getRequestDispatcher("analistaEstudio.jsp");
        rd.forward(request, response);
    }

    private List<String> datosVacios() {
        List<String> datos = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            datos.add("");
        }
        return datos;
    }

    private List<String> separarDatos(String busqueda) {
        List<String> datos = new ArrayList<>();
        datos.addAll(Arrays.asList(busqueda.split(",")));
        datos.set(0, (datos.get(0).equals("-")) ? "" : datos.get(0));
        datos.set(1, datos.get(1));
        datos.set(2, datos.get(2));
        datos.set(3, (datos.get(3).equals("-")) ? "OFF" : "checked");
        datos.set(4, (datos.get(4).equals("-")) ? "OFF" : "checked");
        datos.set(5, "-");
        // Etiquetas en esta posición datos.get(5)
        datos.set(6, datos.get(6).equals("-") ? "" : datos.get(6));
        datos.set(7, datos.get(7).equals("-") ? "" : datos.get(7));
        datos.set(8, (datos.get(8).equals("-")) ? "" : datos.get(8));
        datos.set(9, (datos.get(9).equals("-")) ? "" : datos.get(9));
        datos.set(10, (datos.get(10).equals("-")) ? "0" : datos.get(10));
        datos.set(11, (datos.get(11).equals("-")) ? "0" : datos.get(11));
        datos.set(11, (datos.get(11).equals("0")) ? "-" : datos.get(11));
        datos.set(12, (datos.get(12).equals("-")) ? "" : datos.get(12));
        datos.set(13, (datos.get(13).equals("-")) ? "" : datos.get(13));
        datos.set(14, (datos.get(14).equals("-")) ? "" : datos.get(14));
        return datos;
    }

    private List<Usuario> analizarBaseDeDatos(List<String> datos) {
        List<Usuario> resultados = new ArrayList<>();

        //for (UsuarioDeEventos ude : this.usuarioDeEventosFacade.findAll()) {
        for (Usuario u : this.estudioFacade.filtrarUsuarios(datos)) {
            UsuarioDeEventos ude = u.getUsuarioDeEventos();
            if (ude != null && (datos.get(14).equals("usuariodeeventos") || datos.get(14).isEmpty())) {
                if (edadEnRango(ude.getFechaNacimiento(), datos.get(10), datos.get(11))) {
                    if (filtrosDeEvento(ude, datos)) {
                        resultados.add(u);
                    }
                }
            } else if (noSeUsanFiltrosDeEvento(datos) && !u.getTipoUsuario().equals("usuariodeeventos")) {
                resultados.add(u);
            }
        }
        return resultados;
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private boolean edadEnRango(Date fechaNacimiento, String edad_min, String edad_max) {
        long diff_Fechas = new Date().getTime() - fechaNacimiento.getTime();
        int edad = (int) (diff_Fechas / (1000l * 60 * 60 * 24 * 365));
        if (edad_min.equals("-") || edad_min.isEmpty() || edad > Integer.parseInt(edad_min)) {
            if (edad_max.equals("-") || edad_max.isEmpty() || edad < Integer.parseInt(edad_max)) {
                return true;
            }
        }
        return false;
    }

    private boolean filtrosDeEvento(UsuarioDeEventos ude, List<String> datos) {
        // Este IF comprueba si se usa algún filtro de evento
        //  si no se usa ninguno devuelve true
        if (noSeUsanFiltrosDeEvento(datos)) {
            return true;
        }

        SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
        Date desdeFecha = null;
        Date hastaFecha = null;
        try {
            desdeFecha = (datos.get(1).equals("-")) ? formatoFecha.parse("1970-01-01") : formatoFecha.parse(datos.get(1));
            hastaFecha = (datos.get(2).equals("-")) ? formatoFecha.parse("3000-01-01") : formatoFecha.parse(datos.get(2));
        } catch (ParseException ex) {
            Logger.getLogger(ServletAnalistaListar.class.getName()).log(Level.SEVERE, null, ex);
        }

        // checked = ON
        // Solo se comprueba si los asientos están asignados o si el evento ha finalizado
        // cuando está checked, si no lo está no se comprueba
        boolean asientAsig = datos.get(4).equals("checked");
        boolean eventFinal = datos.get(3).equals("checked");
        Date hoy = new Date();
        List<Evento> listaEventos;
        listaEventos = this.estudioFacade.filtrarEventos(datos.get(0), desdeFecha, hastaFecha, datos.get(6), datos.get(7));
        for (Publico publico : ude.getPublicoList()) {
            Evento evento = publico.getEvento();
            boolean checkAsientosAsig = asientAsig ? evento.getAsientosAsignados() : true;
            boolean checkEventFinalizado = eventFinal ? evento.getFecha().after(hoy) : true;
            if (checkAsientosAsig && checkEventFinalizado && listaEventos.contains(evento)) {
                return true;
            }
        }
        return false;
    }

    private boolean noSeUsanFiltrosDeEvento(List<String> datos) {
        if (datos.get(0).isEmpty() && datos.get(1).equals("-") && datos.get(2).equals("-")
                && datos.get(3).equals("OFF") && datos.get(4).equals("OFF")
                && datos.get(6).equals("") && datos.get(7).isEmpty()) {
            return true;
        } else {
            return false;
        }
    }
}
