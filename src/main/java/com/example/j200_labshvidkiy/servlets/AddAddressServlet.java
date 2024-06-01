package com.example.j200_labshvidkiy.servlets;
import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import com.example.j200_labshvidkiy.Dto.AddressDto;
import com.example.j200_labshvidkiy.Dto.ClientDto;
import com.example.j200_labshvidkiy.repositories.ClientRepository;
import com.example.j200_labshvidkiy.services.AddressService;
import com.example.j200_labshvidkiy.services.ClientService;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "add_address", value = "/add_address")
public class AddAddressServlet extends HttpServlet {
    @EJB
    ClientService clientService;
    @EJB
    AddressService addressService;
    @EJB
    ClientRepository clientRepository;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=utf-8");
        Integer id = Integer.parseInt(request.getParameter("client"));
        ClientDto client = clientService.getClientById(id);
        Collection<AddressDto> addresses = addressService.getAddressesByClientId(id);
        Integer addrRows = Integer.parseInt(Objects.toString(request.getParameter("addr_rows"), "1"))>0?Integer.parseInt(Objects.toString(request.getParameter("addr_rows"), "1")):1;
        request.getSession().setAttribute("addr-rows", addrRows);
        PrintWriter out = response.getWriter();
        out.println("<html><head><meta charset=\"UTF-8\">");
        out.println("<link rel=\"stylesheet\" href=\"style.css\"></head>");
        out.println("<body>");
        out.println(generateTable(client, addresses, addrRows));
        out.println(
                "<script>\n" +
                        "    document.getElementById('form-submit-link').addEventListener('click', function() {\n" +
                        "    document.getElementById('add_address_form').submit();\n" +
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
            addressService.createForClient(AddressDto.builder()
                    .ip(request.getParameter("ip"+(i)))
                    .mac(request.getParameter("mac"+(i)))
                    .model(request.getParameter("model"+(i)))
                    .address(request.getParameter("address"+(i)))
                    .clientid(Integer.parseInt(request.getParameter("client_id")))
                    .build(), clientRepository.findClientById(Integer.parseInt(request.getParameter("client_id"))));
        }

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
    private String generateTable(ClientDto client, Collection<AddressDto> addresses, Integer addrRows){
        StringBuilder table = new StringBuilder();
        table.append("<form id=\"add_address_form\" action=\"add_address\" method=\"post\">\n" +
                "<input type=\"hidden\" name=\"client_id\" value=\""+client.getClientId()+"\">"+
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
                            "         </tr>\n"
            );
        }
        for (int i=0; i<addrRows;i++){
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
                        "                                   <hr><a href=\"add_address?addr_rows="+(addrRows+1)+"&client="+client.getClientId()+"\"><img src=\"elements/add.png\"></a>"+
                        "                                   <a href=\"add_address?addr_rows="+(addrRows-1)+"&client="+client.getClientId()+"\"><img src=\"elements/minus.png\"></a>"+
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