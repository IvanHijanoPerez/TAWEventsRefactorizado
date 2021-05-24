/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tawevents.servlet;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import tawevents.dao.EstudioFacade;
import tawevents.entity.Estudio;
import tawevents.entity.Usuario;

/**
 *
 * @author daniel
 */
@WebServlet(name = "ServletAnalistaCrearEstudio", urlPatterns = {"/ServletAnalistaCrearEstudio"})
public class ServletAnalistaCrearEstudio extends HttpServlet {

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
        
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        //DESCRIPCION NO PUEDE ESTAR VACIA
        String descripcion = request.getParameter("descripcion");
        descripcion = new String(descripcion.getBytes("ISO8859-1"), "UTF8");
        
        String titulo = request.getParameter("titulo"); //
        titulo = ((titulo.isEmpty()) ? "-" : new String(titulo.getBytes("ISO8859-1"), "UTF8"));
        String desdeFecha = request.getParameter("desdeFecha"); //
        desdeFecha = ((desdeFecha.isEmpty()) ? "-" : desdeFecha);
        String hastaFecha = request.getParameter("hastaFecha"); //
        hastaFecha = ((hastaFecha.isEmpty()) ? "-" : hastaFecha);
        String event_fin = request.getParameter("event_fin"); //
        event_fin = ((event_fin == null) ? "-" : event_fin);
        String asient_asig = request.getParameter("asient_asig"); //
        asient_asig = ((asient_asig == null) ? "-" : asient_asig);
        // Hueco para etiquetas
        String aforo_min = request.getParameter("aforo_min"); // 
        aforo_min = ((aforo_min.isEmpty()) ? "-" : aforo_min);
        String aforo_max = request.getParameter("aforo_max"); //
        aforo_max = ((aforo_max.isEmpty()) ? "-" : aforo_max);
        String sexo = request.getParameter("sexo"); //
        sexo = ((sexo == null) ? "-" : sexo);
        String ciudad = request.getParameter("ciudad"); //
        ciudad = ((ciudad == null) ? "" : ciudad);
        ciudad = ((ciudad.isEmpty()) ? "-" : new String(ciudad.getBytes("ISO8859-1"), "UTF8"));
        String edad_min = request.getParameter("edad_min"); //
        edad_min = ((edad_min.isEmpty()) ? "-" : edad_min);
        String edad_max = request.getParameter("edad_max"); //
        edad_max = ((edad_max.isEmpty()) ? "-" : edad_max);
        // CONTROLAR ERRORES - Edad < 0, edad min > edad max - ...
        String nombre = request.getParameter("nombre"); //
        nombre = ((nombre.isEmpty()) ? "-" : new String(nombre.getBytes("ISO8859-1"), "UTF8"));
        String apellidos = request.getParameter("apellidos"); //
        apellidos = ((apellidos.isEmpty()) ? "-" : new String(apellidos.getBytes("ISO8859-1"), "UTF8"));
        String tipousuario = request.getParameter("tipousuario");
        tipousuario = ((tipousuario == null) ? "-" : tipousuario);

        // Compruebo y arreglo las fechas
        SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
        Date desdeF;
        Date hastaF;
        try {
            if (!desdeFecha.equals("-") && !hastaFecha.equals("-")) {
                desdeF = formatoFecha.parse(desdeFecha);
                hastaF = formatoFecha.parse(hastaFecha);
                if (desdeF.after(hastaF)) {
                    String aux = hastaFecha;
                    hastaFecha = desdeFecha;
                    desdeFecha = aux;
                }
            }
        } catch (ParseException ex) {
            Logger.getLogger(ServletAnalistaCrearEstudio.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Compruebo y arreglo los aforos mínimo y máximo
        if (!aforo_min.equals("-") && new Integer(aforo_min) < 0) {
            aforo_min = "-";
        }
        if (!aforo_max.equals("-") && new Integer(aforo_max) < 0) {
            aforo_max = "-";
        }
        if (!aforo_min.equals("-") && !aforo_max.equals("-")) {
            if (new Integer(aforo_min) > new Integer(aforo_max)) {
                String aux = aforo_min;
                aforo_min = aforo_max;
                aforo_max = aux;
            }
        }
        
        // Compruebo y arreglo las edades mínima y máxima
        if (!edad_min.equals("-") && new Integer(edad_min) < 0) {
            edad_min = "-";
        }
        if (!edad_max.equals("-") && new Integer(edad_max) < 0) {
            edad_max = "-";
        }
        if (!edad_min.equals("-") && !edad_max.equals("-")) {
            if (new Integer(edad_min) > new Integer(edad_max)) {
                String aux = edad_min;
                edad_min = edad_max;
                edad_max = aux;
            }
        }

        // Unir datos
        StringBuilder busqueda = new StringBuilder("");
        busqueda.append(titulo).append(",");
        busqueda.append(desdeFecha).append(",");
        busqueda.append(hastaFecha).append(",");
        busqueda.append(event_fin).append(",");
        busqueda.append(asient_asig).append(",");
        busqueda.append("-,"); //etiquetas
        busqueda.append(aforo_min).append(",");
        busqueda.append(aforo_max).append(",");
        busqueda.append(sexo).append(",");
        busqueda.append(ciudad).append(",");
        busqueda.append(edad_min).append(",");
        busqueda.append(edad_max).append(",");
        busqueda.append(nombre).append(",");
        busqueda.append(apellidos).append(",");
        busqueda.append(tipousuario);

        // Creo e inserto el nuevo estudio
        Estudio estudio = new Estudio();
        estudio.setAnalista(usuario);
        estudio.setFecha(new Date());
        estudio.setDescripcion(descripcion);
        estudio.setBusqueda(busqueda.toString());

        this.estudioFacade.create(estudio);

        response.sendRedirect("ServletAnalistaListar");
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
