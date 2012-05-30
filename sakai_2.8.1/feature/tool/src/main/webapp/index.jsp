<jsp:directive.include file="/templates/includes.jsp"/>
<jsp:directive.include file="/templates/header.jsp"/>
Hello World Servlet </br>
Current Site ID : ${currentSiteId} </br>
User Display Name : ${userDisplayName}

<div id="cdm_editor">
<table>
  <tr>
    <td>Titre du cours: </td>
    <td><span id="course_title" name="title"/></td>
  </tr>
  <tr class="row_space" />
  <tr>
    <td>Description du cours</td>
    <td><textarea id="editor_area"></textarea></td>    
  </tr>
  <tr class="row_space" />
</table>
<div id ="div_buttons">
<span id="save_button" class="button">Sauvegarder</span>
<span id="cancel_button" class="button">Annuler</span>
</div>
</div>

<table id="table_id">
    <thead>
        <tr>
            <th>Identifiant du cours</th>
			<th>Titre du cours</th>
			<th>Programme d'études</th>
            <th>Description</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td>1-404-96</td>
            <td>Sociologie de l'entreprise</td>
			<td>B.A.A</td>
            <td><span  class="hidden_description_flag">1</span></td>
        </tr>
        <tr>
            <td>1-901-06</td>
			<td>Présentation de l'information comptable</td>
            <td>B.A.A</td>
            <td><span  class="hidden_description_flag">1</span></td>
        </tr>
		<tr>
            <td>1-928-05</td>
			<td>Using Accounting Information (offert en anglais)</td>
            <td>B.A.A</td>
            <td><span  class="hidden_description_flag">0</span></td>
        </tr>
		<tr>
            <td>2-014-07</td>
			<td>Commerce international</td>
            <td>B.A.A</td>
            <td><span  class="hidden_description_flag">0</span></td>
        </tr>
		<tr>
            <td>2-024-07</td>
			<td>Marketing international</td>
            <td>B.A.A</td>
            <td><span  class="hidden_description_flag">0</span></td>
        </tr>
    </tbody>
</table>


<div id="ajaxMessage">
<jsp:directive.include file="/templates/footer.jsp"/>

