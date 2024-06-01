package com.example.j200_labshvidkiy.servlets;
import java.io.*;
import java.util.Collection;

import com.example.j200_labshvidkiy.dto.AddressDto;
import com.example.j200_labshvidkiy.dto.ClientDto;
import com.example.j200_labshvidkiy.services.AddressService;
import com.example.j200_labshvidkiy.services.ClientService;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "delete_client", value = "/delete_client")
public class DeleteClientServlet extends HttpServlet {
    @EJB
    ClientService clientService;
    @EJB
    AddressService addressService;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=utf-8");
        Integer id = Integer.parseInt(request.getParameter("client"));
        ClientDto client = clientService.getClientById(id);
        Collection<AddressDto> addresses = addressService.getAddressesByClientId(id);
        Integer addrRows = addresses.size();
        PrintWriter out = response.getWriter();
        out.println("<html><head><meta charset=\"UTF-8\">");
        out.println("<link rel=\"stylesheet\" href=\"style.css\"></head>");
        out.println("<body>");
        out.println(generateTable(client, addresses));
        out.println(
                "<script>\n" +
                "    document.getElementById('form-submit-link').addEventListener('click', function() {\n" +
                "    document.getElementById('delete_client_form').submit();\n" +
                "    });\n" +
                "</script>"
        );
        out.println("</body></html>");

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");

        Integer clientId = Integer.parseInt(request.getParameter("client_id"));
        clientService.removeById(clientId);

        PrintWriter out = response.getWriter();
        out.println("<html><head><meta charset=\"UTF-8\">");
        out.println("<link rel=\"stylesheet\" href=\"style.css\"></head>");
        out.println("<body>");
        //out.println(generateTable());
        out.println("</body></html>");
        response.sendRedirect("main_view");
    }

    public void destroy() {
    }
    private String generateTable(ClientDto client, Collection<AddressDto> addresses){
        StringBuilder table = new StringBuilder();
        table.append("<form id=\"delete_client_form\" action=\"delete_client\" method=\"post\">\n" +
                "<input name=\"client_id\" type=\"hidden\" value=\""+client.getClientId()+"\">"+
                "<table>\n" +
                "    <tr>\n" +
                "        <td>\n" +
                "            <table class=\"unit\">\n" +
                "                <tr>\n" +
                "                    <td id=\"keyname1\">Имя:</td>\n" +
                "                    <td id=\"valuename1\"><p>"+client.getClientName()+"</p></td>\n" +
                "                </tr>\n" +
                "                <tr>\n" +
                "                    <td id=\"keytype1\">Тип клиента:</td>\n" +
                "                    <td id=\"valuetype1\"><p>"+client.getClientType()+"</p></td>\n" +
                "                </tr>\n" +
                "                <tr>\n" +
                "                    <td id=\"keydate1\">Дата добавления:</td>\n" +
                "                    <td id=\"valuedate1\"><p>"+client.getAdded()+"</p></td>\n" +
                "                </tr>\n" +
                "            </table>\n" +
                "        </td>\n" +
                "        <td>\n" +
                "            <table class=\"address\">\n" +
                "                <tr>\n" +
                "                    <td>\n" +
                "                        <table id=\"create_subaddress\">\n" +
                "                            <tr>\n" +
                "                                <th id=\"addrip1\">IP-адрес</th>\n" +
                "                                <th id=\"addrmac1\">MAC-адрес</th>\n" +
                "                                <th id=\"addrmodel1\">Модель устройства</th>\n" +
                "                                <th id=\"addraddr1\">Адрес</th>\n" +
                "                                <th></th>\n" +
                "                            </tr>\n");
        for (AddressDto addr:addresses){
            table.append(
                    "                <tr>\n" +
                            "                        <td><p>"+addr.getIp()+"</p></td>\n" +
                            "                        <td><p>"+addr.getMac()+"</p></td>\n" +
                            "                        <td><p>"+addr.getModel()+"</p></td>\n" +
                            "                        <td><p>"+addr.getAddress()+"</p></td>\n" +
                            "                        <td></td>\n" +
                            "                 </tr>\n"
            );
        }
        table.append(
                        "                        </table>\n" +
                        "                    </td>\n" +
                        "                </tr>\n" +
                        "            </table>\n" +
                        "        </td>\n" +
                        "    </tr>\n" +
                        "    <tr>\n" +
                        "        <td><h1 class=\"warning_header\">Удалить клиента?</h1></td>\n" +
                        "    </tr>\n" +
                        "    <tr>\n" +
                        "        <td colspan=\"2\">\n"+
                        "            <a href=\"javascript:void(0);\" id=\"form-submit-link\" class=\"btn\">Удалить клиента</a>"+
                        "            <a href=\"main_view\" class=\"btn\">Отмена</a>\n" +
                        "        </td>\n" +
                        "    </tr>"+
                        "</table></form>");
        return table.toString();
    }

}