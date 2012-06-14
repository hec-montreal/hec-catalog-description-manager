<jsp:directive.include file="/templates/includes.jsp" />
<jsp:directive.include file="/templates/header.jsp" />
<h3>Descriptions annuaires éditables par l'utilisateur :
	${userDisplayName}</h3>

<br />
<div id="cdm_editor">
	<div id="accordeonWrap">
		<h3>
			<a href="#">Description du cours</a>
		</h3>
		<div>
			<textarea id="editor_area"></textarea>
		</div>
		<h3>
			<a href="#">Informations du cours</a>
		</h3>
		<div>
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
	<div id="div_buttons" style="clear: both;">
		<br /> <span id="save_button" class="button">Sauvegarder</span> <span
			id="cancel_button" class="button">Annuler</span>
	</div>
</div>


<table id="catalog_description_table">
	<thead>
		<tr>
			<th>Id</th>
			<th>CourseId</th>
			<th>Title</th>
			<th>Department</th>
			<th>Career</th>
			<th>Language</th>
			<th>Description</th>
		</tr>
	</thead>
	<tbody>
	</tbody>
</table>
<input type="hidden" id="course_id" />



<div id="ajaxReturn">
	<div id="ajaxMessage" class="return_message"></div>
</div>
<jsp:directive.include file="/templates/footer.jsp" />