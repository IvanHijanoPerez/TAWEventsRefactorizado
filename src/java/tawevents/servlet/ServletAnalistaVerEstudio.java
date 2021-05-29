package tawevents.servlet;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import tawevents.dao.EventoFacade;
import tawevents.dao.UsuarioDeEventosFacade;
import tawevents.dto.EstudioDTO;
import tawevents.dto.UsuarioDTO;
import tawevents.entity.Usuario;
import tawevents.service.EstudioService;

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
    private EstudioService estudioService;

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

        // Comprobar que el usuario está conectado y es analita de eventos
        HttpSession session = request.getSession();
        UsuarioDTO usuario = (UsuarioDTO) session.getAttribute("usuario");

        if (usuario == null || !usuario.getTipoUsuario().equals("analistadeeventos")) {
            RequestDispatcher rd = request.getRequestDispatcher("inicioSesion.jsp");
            rd.forward(request, response);
        }

        String modo = null;
        EstudioDTO estudio = null;
        List<String> datos = null;
        Set<String> ciudades = null;

        modo = request.getParameter("modo");
        String strID = request.getParameter("id");
        if (strID != null) {
            estudio = this.estudioService.encontrarEstudioPorID(Integer.parseInt(strID));
            datos = this.estudioService.separarDatos(estudio.getBusqueda());
        } else {
            datos = this.estudioService.datosVacios();
        }

        if (modo.equals("crear")) {
            ciudades = this.estudioService.encontrarCiudadesParaCrearEstudio();
            request.setAttribute("ciudades", ciudades);
        } else if (modo.equals("ver")) {
            List<Usuario> resultados = this.estudioService.analizarBaseDeDatos(datos); // USUARIO DTO
            request.setAttribute("resultados", resultados);
        }
        request.setAttribute("estudio", estudio);
        request.setAttribute("datos", datos);
        request.setAttribute("modo", modo);

        RequestDispatcher rd = request.getRequestDispatcher("analistaEstudio.jsp");
        rd.forward(request, response);
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
}
