package com.example.j200_labshvidkiy.servlets;
import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import com.example.j200_labshvidkiy.Dto.AddressDto;
import com.example.j200_labshvidkiy.Dto.ClientDto;
import com.example.j200_labshvidkiy.services.AddressService;
import com.example.j200_labshvidkiy.services.ClientService;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "delete_address", value = "/delete_address")
public class DeleteAddressServlet extends HttpServlet {
    @EJB
    ClientService clientService;
    @EJB
    AddressService addressService;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=utf-8");
        Integer clientId = Integer.parseInt(request.getParameter("client_id"));
        Integer addressId = Integer.parseInt(request.getParameter("address_id"));
        ClientDto client = clientService.getClientById(clientId);
        Collection<AddressDto> addresses = addressService.getAddressesByClientId(clientId);
        Integer addrRows = addresses.size();
        PrintWriter out = response.getWriter();
        out.println("<html><head><meta charset=\"UTF-8\">");
        out.println("<link rel=\"stylesheet\" href=\"style.css\"></head>");
        out.println("<body>");
        out.println(generateTable(client, addresses, addressId));
        out.println(
                "<script>\n" +
                        "    document.getElementById('form-submit-link').addEventListener('click', function() {\n" +
                        "    document.getElementById('delete_address_form').submit();\n" +
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

        Integer selected = Integer.parseInt(request.getParameter("selected"));
        addressService.removeById(selected);
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
    private String generateTable(ClientDto client, Collection<AddressDto> addresses, Integer selected){
        StringBuilder table = new StringBuilder();
        table.append("<form id=\"delete_address_form\" action=\"delete_address\" method=\"post\">\n" +
                "<input name=\"selected\" type=\"hidden\" value=\""+selected+"\">"+
                "<table>\n" +
                "    <tr>\n" +
                "        <td>\n" +
                "            <table class=\"unit\">\n" +
                "                <tr>\n" +
                "                    <td id=\"keyid1\">ID:</td>\n" +
                "                    <td id=\"valuename1\"><p>"+client.getClientId()+"</p></td>\n" +
                "                </tr>\n" +
                "                <tr>\n" +
                "                    <td id=\"keyname1\">Имя:</td>\n" +
                "                    <td id=\"valuename1\"><p>"+client.getClientName()+"</p></td>\n" +
                "                </tr>\n" +
                "                <tr>\n" +
                "                    <td id=\"keytype1\">Тип клиента:</td>\n" +
                "                    <td id=\"valuetype1\">"+client.getClientType()+"</p></td>\n" +
                "                </tr>\n" +
                "                <tr>\n" +
                "                    <td id=\"keydate1\">Дата добавления:</td>\n"+
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
                    "                         <tr "+(addr.getAddressid()==selected?"bgcolor=\"lightcoral\"":"")+">\n" +
                            "                        <td><p>"+addr.getIp()+"</p></td>\n" +
                            "                        <td><p>"+addr.getMac()+"</p></td>\n" +
                            "                        <td><p>"+addr.getModel()+"</p></td>\n" +
                            "                        <td><p>"+addr.getAddress()+"</p></td>\n" +
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
                        "        <td><h1 class=\"warning_header\">Удалить адрес?</h1></td>\n" +
                        "    </tr>\n" +
                        "    <tr>\n" +
                        "        <td colspan=\"2\">" +
                        "            <a href=\"javascript:void(0);\" id=\"form-submit-link\" class=\"btn\">Удалить адрес</a>"+
                        "            <a href=\"main_view\" class=\"btn\">Отмена</a>\n" +
                        "        </td>\n" +
                        "    </tr>\n" +
                        "</table></form>");
        return table.toString();
    }

}