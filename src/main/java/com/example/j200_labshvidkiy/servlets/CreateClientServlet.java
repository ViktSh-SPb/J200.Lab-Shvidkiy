package com.example.j200_labshvidkiy.servlets;
import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import com.example.j200_labshvidkiy.dto.AddressDto;
import com.example.j200_labshvidkiy.dto.ClientDto;
import com.example.j200_labshvidkiy.services.ClientService;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "create_servlet", value = "/create_servlet")
public class CreateClientServlet extends HttpServlet {
    @EJB
    ClientService clientService;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=utf-8");
        Integer addrRows = Integer.parseInt(Objects.toString(request.getParameter("addr_rows"), "1"))>0?Integer.parseInt(Objects.toString(request.getParameter("addr_rows"), "1")):1;
        request.getSession().setAttribute("addr-rows", addrRows);
        PrintWriter out = response.getWriter();
        out.println("<html><head><meta charset=\"UTF-8\">");
        out.println("<link rel=\"stylesheet\" href=\"style.css\"></head>");
        out.println("<body>");
        out.println(generateTable(addrRows));
        if(request.getParameter("err_msg")!=null){
            switch (request.getParameter("err_msg")){
                case("name"):
                    out.println("<h1 class=\"warning_header\">Неправильно введено имя. Имя может содержать буквы Кириллицы и знаки .,- и пробел.</h1>");
                    break;
                case ("type"):
                    out.println("<h1 class=\"warning_header\">Не выбран тип клиента</h1>");
                    break;
                case ("added"):
                    out.println("<h1 class=\"warning_header\">Дата регистрации должна быть заполнена и быть меньше текущей</h1>");
                    break;
                case ("ip"):
                    out.println("<h1 class=\"warning_header\">Некорректный ввод IP. IP-адрес должен иметь вид: \"***.***.***.***(192.168.0.1)\"</h1>");
                    break;
                case ("mac"):
                    out.println("<h1 class=\"warning_header\">Некорректно введен MAC-адрес. MAC-адрес должен иметь вид: \"**-**-**-**-**-**(FF-11-22-33-44-55)\"</h1>");
                    break;
                case ("model"):
                    out.println("<h1 class=\"warning_header\">Некорректно введена модель. Название модели должно быть не менее 1 и не более 100 символов</h1>");
                    break;
                case ("address"):
                    out.println("<h1 class=\"warning_header\">Некорректно введен адрес. Адрес должен состоять из символов Кириллицы и не превышать 200 символов</h1>");
                    break;
            }


        }
        out.println("</body></html>");

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");

        Integer addrRows = Integer.parseInt(Objects.toString(request.getSession().getAttribute("addr-rows"), "1")) > 0 ? Integer.parseInt(Objects.toString(request.getSession().getAttribute("addr-rows"), "1")) : 1;
        Collection<AddressDto> addresses = new ArrayList<>();

        for (int i = 0; i < addrRows; i++) {
            addresses.add(AddressDto.builder()
                    .ip(request.getParameter("ip" + (i)))
                    .mac(request.getParameter("mac" + (i)))
                    .model(request.getParameter("model" + (i)))
                    .address(request.getParameter("address" + (i)))
                    .build());
        }
        ClientDto client = ClientDto.builder()
                .clientName(request.getParameter("client_name"))
                .clientType(request.getParameter("client_type"))
                .added(request.getParameter("added"))
                .build();
        if (clientService.checkClient(client, addresses) != "ok") {
            response.sendRedirect("create_servlet?err_msg=" + clientService.checkClient(client, addresses));
        } else {
            clientService.
                    create(ClientDto.builder()
                            .clientName(request.getParameter("client_name"))
                            .clientType(request.getParameter("client_type"))
                            .added(request.getParameter("added"))
                            .build(), addresses);

            PrintWriter out = response.getWriter();
            out.println("<html><head><meta charset=\"UTF-8\">");
            out.println("<link rel=\"stylesheet\" href=\"style.css\"></head>");
            out.println("<body>");
            out.println(generateTable(addrRows));
            out.println("</body></html>");
            response.sendRedirect("main_view");
        }
    }
    public void destroy() {
    }
    private String generateTable(int addrRows){
        StringBuilder table = new StringBuilder();
        table.append("<form id=\"create_form\" action=\"create_servlet\" method=\"post\">\n" +
                "<table>\n" +
                "    <tr>\n" +
                "        <td>\n" +
                "            <table id=\"unit1\">\n" +
                "                <tr>\n" +
                "                    <td id=\"keyname1\">Имя:</td>\n" +
                "                    <td id=\"valuename1\"><input name=\"client_name\" type=\"text\" size=\"50\"></td>\n" +
                "                </tr>\n" +
                "                <tr>\n" +
                "                    <td id=\"keytype1\">Тип клиента:</td>\n" +
                "                    <td id=\"valuetype1\">\n" +
                "                        <select name=\"client_type\">\n" +
                "                            <option>физическое лицо</option>\n" +
                "                            <option>юридическое лицо</option>\n" +
                "                        </select>\n" +
                "                    </td>\n" +
                "                </tr>\n" +
                "                <tr>\n" +
                "                    <td id=\"keydate1\">Дата добавления:</td>\n" +
                "                    <td id=\"valuedate1\"><input name=\"added\" type=\"date\"></td>\n" +
                "                </tr>\n" +
                "            </table>\n" +
                "        </td>\n" +
                "        <td>\n" +
                "            <table id=\"create_address\">\n" +
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
        for (int i=0; i<addrRows; i++){
            table.append(
                            "                <tr>\n" +
                            "                        <td><input name=\"ip"+i+"\" size=\"30\"></td>\n" +
                            "                        <td><input name=\"mac"+i+"\" size=\"40\"></td>\n" +
                            "                        <td><input name=\"model"+i+"\" size=\"40\"></td>\n" +
                            "                        <td><input name=\"address"+i+"\" size=\"70\"></td>\n" +
                            "                        <td></td>\n" +
                            "                 </tr>\n"
            );
        }
        table.append(
                "                            <tr>\n" +
                        "                                <td colspan=\"4\" align=\"center\">"+
                        "                                   <hr><a href=\"create_servlet?addr_rows="+(addrRows+1)+"\"><img src=\"elements/add.png\"></a>"+
                        "                                   <a href=\"create_servlet?addr_rows="+(addrRows-1)+"\"><img src=\"elements/minus.png\"></a>"+
                        "                                </td>" +
                        "                            </tr>\n" +
                        "                        </table>\n" +
                        "                    </td>\n" +
                        "                </tr>\n" +
                        "            </table>\n" +
                        "        </td>\n" +
                        "    </tr>\n" +
                        "    <tr>\n" +
                        "        <td colspan=\"2\"><input type=\"submit\" value=\"Создать клиента\"></td>\n" +
                        "    </tr>\n" +
                        "</table></form>");
        return table.toString();
    }
}