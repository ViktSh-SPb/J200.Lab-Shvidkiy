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

@WebServlet(name = "edit_client", value = "/edit_client")
public class EditClientServlet extends HttpServlet {
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
        request.getSession().setAttribute("addr-rows", addrRows);
        PrintWriter out = response.getWriter();
        out.println("<html><head><meta charset=\"UTF-8\">");
        out.println("<link rel=\"stylesheet\" href=\"style.css\"></head>");
        out.println("<body>");
        out.println(generateTable(client, addresses));
        out.println(
                "<script>\n" +
                        "    document.getElementById('form-submit-link').addEventListener('click', function() {\n" +
                        "    document.getElementById('edit_client_form').submit();\n" +
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

        Integer addrRows = Integer.parseInt(Objects.toString(request.getSession().getAttribute("addr-rows"),"1"))>0?Integer.parseInt(Objects.toString(request.getSession().getAttribute("addr-rows"), "1")):1;
        Collection<AddressDto> addresses= new ArrayList<>();

        for (int i=0; i<addrRows; i++){
            addresses.add(AddressDto.builder()
                    .addressid(Integer.parseInt(request.getParameter("id"+(i))))
                    .ip(request.getParameter("ip"+(i)))
                    .mac(request.getParameter("mac"+(i)))
                    .model(request.getParameter("model"+(i)))
                    .address(request.getParameter("address"+(i)))
                    .clientid(Integer.parseInt(request.getParameter("client_id")))
                    .build());
        }
        clientService.update(ClientDto.builder()
                .clientId(Integer.parseInt(request.getParameter("client_id")))
                .clientName(request.getParameter("client_name"))
                .clientType(request.getParameter("client_type"))
                .added(request.getParameter("added"))
                .build(), addresses);

        PrintWriter out = response.getWriter();
        out.println("<html><head><meta charset=\"UTF-8\">");
        out.println("<link rel=\"stylesheet\" href=\"style.css\"></head>");
        out.println("<body>");
        out.println("</body></html>");
        response.sendRedirect("main_view");
    }

    public void destroy() {
    }
    private String generateTable(ClientDto client, Collection<AddressDto> addresses){
        StringBuilder table = new StringBuilder();
        table.append("<form id=\"edit_client_form\" action=\"edit_client\" method=\"post\">\n" +
                "<input type=\"hidden\" name=\"client_id\" value=\""+client.getClientId()+"\">"+
                "<table>\n" +
                "    <tr>\n" +
                "        <td>\n" +
                "            <table class=\"unit\">\n" +
                "                <tr>\n" +
                "                    <td id=\"keyname1\">Имя:</td>\n" +
                "                    <td id=\"valuename1\"><input name=\"client_name\" type=\"text\" size=\"50\" value = \""+client.getClientName()+"\"></td>\n" +
                "                </tr>\n" +
                "                <tr>\n" +
                "                    <td id=\"keytype1\">Тип клиента:</td>\n" +
                "                    <td id=\"valuetype1\">\n" +
                "                        <select name=\"client_type\">\n" +
                "                            <option "+(client.getClientType().equals("физическое лицо")?"selected":"")+">физическое лицо</option>\n" +
                "                            <option "+(client.getClientType().equals("юридическое лицо")?"selected":"")+">юридическое лицо</option>\n" +
                "                        </select>\n" +
                "                    </td>\n" +
                "                </tr>\n" +
                "                <tr>\n" +
                "                    <td id=\"keydate1\">Дата добавления:</td>\n" +
                "                    <td id=\"valuedate1\"><input name=\"added\" type=\"date\" value=\""+client.getAdded()+"\"></td>\n" +
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
        int i=0;
        for (AddressDto addr:addresses){
            table.append(
                            "                 <tr>\n" +
                            "                        <input name=\"id"+i+"\" type=\"hidden\" value=\""+addr.getAddressid()+"\">"+
                            "                        <td><input name=\"ip"+i+"\" size=\"30\" type=\"text\" value=\""+addr.getIp()+"\"></td>\n" +
                            "                        <td><input name=\"mac"+i+"\" size=\"40\" type=\"text\" value=\""+addr.getMac()+"\"></td>\n" +
                            "                        <td><input name=\"model"+i+"\" size=\"40\" type=\"text\" value=\""+addr.getModel()+"\"></td>\n" +
                            "                        <td><input name=\"address"+i+"\" size=\"70\" type=\"text\" value=\""+addr.getAddress()+"\"></td>\n" +
                            "                        <td></td>\n" +
                            "                 </tr>\n"
            );
            i++;
        }
        table.append(
                "                            <tr>\n" +
                        "                                <td colspan=\"4\" align=\"center\">"+
                        "                                   <hr><a href=\"add_address?addr_rows=1&client="+client.getClientId()+"\"><img src=\"elements/add.png\"></a>"+
                        "                                </td>" +
                        "                            </tr>\n" +
                        "                        </table>\n" +
                        "                    </td>\n" +
                        "                </tr>\n" +
                        "            </table>\n" +
                        "        </td>\n" +
                        "    </tr>\n" +
                        "    <tr>\n" +
                        "        <td colspan=\"2\">" +
                        "            <a href=\"javascript:void(0);\" id=\"form-submit-link\" class=\"btn\">Сохранить изменения</a>"+
                        "            <a href=\"main_view\" class=\"btn\">Отмена</a>\n" +
                        "        </td>\n" +
                        "    </tr>\n" +
                        "</table></form>");
        return table.toString();
    }

}