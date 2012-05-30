<jsp:directive.include file="/templates/includes.jsp"/>
<jsp:directive.include file="/templates/header.jsp"/>
<h3>Descriptions annuaires éditables par l'utilisateur : ${userDisplayName}</h3>

<br/> 
<div id="cdm_editor">
<div>
<div class="first_div" > 
<h2 class="tableau_title">Description du cours</h2></td>
<textarea id="editor_area"></textarea>
</div>
<div class="second_div" style="" border="1">
<h2 class="tableau_title"> Informations du cours</h2>
<table>
			  <tr class="row_space" />
			  <tr>
				<td><span class="td_title">Responsable: </span></td>
				<td>Management</td>
			  </tr>
			  <tr class="row_space" />
			  <tr>
				<td><span class="td_title">Programme d'étude: </span></td>
				<td>B.A.A</td>
			  </tr>
			  <tr class="row_space" />
			  <tr>
				<td><span class="td_title">Crédits: </span></td>
				<td>3</td>
			  </tr>
			  <tr class="row_space" />
			  <tr>
				<td><span class="td_title">Exigences: </span></td>
				<td>non</td>
			  </tr>
			</table>
</div>
</div>  
<div id ="div_buttons" style="clear:both;">
<br/>
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

