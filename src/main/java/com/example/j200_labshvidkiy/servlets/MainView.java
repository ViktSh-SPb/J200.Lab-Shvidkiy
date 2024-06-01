package com.example.j200_labshvidkiy.servlets;

import com.example.j200_labshvidkiy.entities.AddressEntity;
import com.example.j200_labshvidkiy.entities.ClientEntity;
import com.example.j200_labshvidkiy.repositories.ClientRepository;
import com.example.j200_labshvidkiy.services.ClientService;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

@WebServlet(name = "main_view", value = "/main_view")
public class MainView extends HttpServlet {
    @EJB
    private ClientRepository clientRep;
    @EJB
    ClientService clientService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        String clientName = Objects.requireNonNullElse(request.getParameter("client_name"),"");
        String clientType = Objects.requireNonNullElse(request.getParameter("client_type"),"");
        String clientIp = Objects.requireNonNullElse(request.getParameter("ip_address"),"");
        System.out.println("clName: "+clientName);
        PrintWriter out = response.getWriter();
        out.println("<html>\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <link rel=\"stylesheet\" href=\"style.css\">\n" +
                "    <title>Клиенты</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>Список клиентов<h1>"+
                "<table>"+
                "   <tr>"+
                "       <td><a href=\"create_servlet?addr_rows=1\" class=\"btn\">Создать клиента</a></td>"+
                "       <td>\n" +
                "            <table class=\"search_form\">\n" +
                "               <form name=\"search_form\" id=\"search\" action=\"main_view\" method=\"get\">"+
                "                <tr>\n" +
                "                    <td>Имя клиента: <input type=\"text\" name=\"client_name\"></td>\n" +
                "                    <td>Тип клиента: <select name=\"client_type\">\n" +
                "                        <option selected value=\"\"></option>\n" +
                "                        <option value=\"физическое лицо\">физическое лицо</option>\n" +
                "                        <option value=\"юридическое лицо\">юридическое лицо</option>\n" +
                "                    </select></td>\n" +
                "                    <td>IP-адрес: <input class=\"ip\" type=\"text\" name=\"ip_address\"></td>\n" +
                "                    <td><a href=\"javascript:void(0);\" id=\"form-submit-link\" class=\"btn\">Найти</a></td>\n" +
                "                </tr>\n" +
                "               </form>"+
                "            </table>\n" +
                "        </td>"+
                "   </tr>"+
                "</table>"+
                generateTable(clientName, clientType, clientIp)+
                "<script>\n" +
                "    document.getElementById('form-submit-link').addEventListener('click', function() {\n" +
                "        document.getElementById('search').submit();\n" +
                "    });\n" +
                "</script>"+
                "</body>\n" +
                "</html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    public String generateTable(String clientName, String clientType, String ip){
        System.out.println("params: "+clientName+","+clientType+","+ip);
        Predicate<ClientEntity>checkName = i->i.getClientName().contains(clientName);
        if(clientName=="") checkName = i->true;
        Predicate<ClientEntity>checkType = i->i.getClienttype().equals(clientType);
        if(clientType=="") checkType = i->true;
        Predicate<ClientEntity>checkIp = i->clientService.findIp(i,ip);
        if(ip=="") checkIp = i->true;
        List<ClientEntity> clients = clientRep
                .findAll()
                .stream()
                .filter(checkName)
                .filter(checkType)
                .filter(checkIp)
                .toList();
        StringBuilder table = new StringBuilder();
        for (ClientEntity client:clients){
            table.append("<table>\n" +
                    "    <tr>\n" +
                    "        <td>\n" +
                    "            <table class=\"unit\">\n" +
                    "                <tr>\n" +
                    "                    <td id=\"keyid"+client.getClientid()+"\">ID:</td>\n" +
                    "                    <td id=\"valueid"+client.getClientid()+"\">"+client.getClientid()+"</td>\n" +
                    "                </tr>\n" +
                    "                <tr>\n" +
                    "                    <td id=\"keyname"+client.getClientid()+"\">Имя:</td>\n" +
                    "                    <td id=\"valuename"+client.getClientid()+"\">"+client.getClientName()+"</td>\n" +
                    "                </tr>\n" +
                    "                <tr>\n" +
                    "                    <td id=\"keytype"+client.getClientid()+"\">Тип клиента:</td>\n" +
                    "                    <td id=\"valuetype"+client.getClientid()+"\">"+client.getClienttype()+"</td>\n" +
                    "                </tr>\n" +
                    "                <tr>\n" +
                    "                    <td id=\"keydate"+client.getClientid()+"\">Дата добавления:</td>\n" +
                    "                    <td id=\"valuedate"+client.getClientid()+"\">"+client.getAdded()+"</td>\n"+
                    "                </tr>\n" +
                    "                <tr>\n" +
                    "                    <td align=\"center\" colspan=\"2\" id=\"buttons"+client.getClientid()+"\"><a href=\"edit_client?client="+client.getClientid()+"\"><img src=\"elements/edit.png\"></a>&nbsp;<a href=\"delete_client?client="+client.getClientid()+"\"><img src=\"elements/trash.png\"></a></td>\n" +
                    "                </tr>\n" +
                    "            </table>\n" +
                    "        </td>\n" +
                    "        <td>\n" +
                    "            <table class=\"address\">\n" +
                    "                <tr>\n" +
                    "                    <td>\n" +
                    "                        <table id=\"subaddress"+client.getClientid()+"\">\n" +
                    "                            <tr>\n" +
                    "                                <th id=\"addrip"+client.getClientid()+"\">IP-адрес</th>\n" +
                    "                                <th id=\"addrmac"+client.getClientid()+"\">MAC-адрес</th>\n" +
                    "                                <th id=\"addrmodel"+client.getClientid()+"\">Модель устройства</th>\n" +
                    "                                <th id=\"addraddr"+client.getClientid()+"\">Адрес</th>\n" +
                    "                                <th></th>\n" +
                    "                            </tr>\n");
                    for(AddressEntity address: client.getAddressesByClientid()){
                        table.append("                                <tr>\n" +
                                     "                                <td>"+address.getIp()+"</td>\n" +
                                     "                                <td>"+address.getMac()+"</td>\n" +
                                     "                                <td>"+address.getModel()+"</td>\n" +
                                     "                                <td>"+address.getAddress()+"</td>\n"+
                                     "                                <td><a href=\"edit_address?address_id="+address.getAddressid()+"&client_id="+client.getClientid()+"\"><img src=\"elements/edit.png\"></a><a href=\"delete_address?address_id="+address.getAddressid()+"&client_id="+client.getClientid()+"\"><img src=\"elements/trash.png\"></a></td>\n" +
                                     "                                </tr>\n"
                                );
                    }
                    table.append("<tr>\n" +
                            "                                <td colspan=\"4\" align=\"center\"><hr><a href=\"add_address?addr_rows=1&client="+client.getClientid()+"\"><img src=\"elements/add.png\" h></td>\n" +
                            "                            </tr>\n" +
                            "                        </table>\n" +
                            "                    </td>\n" +
                            "                </tr>\n" +
                            "            </table>\n" +
                            "        </td>\n" +
                            "    </tr>\n" +
                            "</table>"
                    );
        }

        return table.toString();
    }
}
