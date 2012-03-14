<html>
  <head>
    <title>Jersey-Test</title>
    <script type="text/javascript" src="jquery-1.3.2.js"></script>
    <script type="text/javascript"> 
    
      // Handler installieren, sobald das Dokument geladen wurde

      $(document).ready(function() {
        $("li.status").ajaxError(handleLoadCustomerError);
      });
      
      // Funktionen

      function loadCustomer() {
        $.getJSON("rest/customers/"+$("[name=customerId]").val(), 
        function(customer) { 
          $("td.vorname").replaceWith("<td class='vorname'>"+customer.vorname+"</td>"); 
          $("td.nachname").replaceWith("<td class='nachname'>"+customer.nachname+"</td>"); 
          $("li.status").replaceWith("<li class='status'>ok</li>"); 
        });
      }

      function handleLoadCustomerError(request, settings, exception) {
        $("td.vorname").replaceWith("<td class='vorname'></td>"); 
        $("td.nachname").replaceWith("<td class='nachname'></td>"); 
        $("li.status").replaceWith("<li class='status'>Kundendaten konnten nicht geladen werden</li>"); 
      }
    </script>
  </head>
  
  <body>
    <h2>Jersey-Test</h2>
    <ul>
      <li>JSP rendered at <%= new java.util.Date() %>
      <li><a href="rest/customers/changes">letzte Kundenaenderungen (text/html)</a></li>
      <li><a href="rest/customers/{id}">gib Kunde {id} zurueck (application/json)</a></li>
    </ul>
    
    <table>
      <tr>
        <td>ID: <input type="text" name="customerId"></td>
        <td><input type="button" value="hole Kundendaten" onclick="loadCustomer()"></td>
      </tr>
      <tr>
        <td>Vorname</td><td class="vorname"></td>
      </tr>
      <tr>
        <td>Nachname</td><td class="nachname"></td>
      </tr>
    </table>
    <ul>
      <li class="status">ok</li>
    </ul>
  </body>
</html>

