<jsp:directive.include file="/templates/includes.jsp" />
<jsp:directive.include file="/templates/header.jsp" />
<div id="ajaxReturn">
	<div id="ajaxMessage" class="return_message"></div>
</div>
<h3>
	<c:out value="${msgs.message_home}" />
</h3>

<br />
<div id="cdm_editor">
	<div id="accordeonWrap">
		<h3>
			<a href="#"><c:out value="${msgs.label_description}" /></a>
		</h3>
		<div>
			<textarea id="editor_area"></textarea>
		</div>
		<h3>
			<a href="#"><c:out value="${msgs.label_course_info}" /></a>
		</h3>
		<div>
			<table class="course_info">
				<tr class="row_space" />
				<tr>
					<td><span class="td_title"><c:out
								value="${msgs.label_acad_department}" /> </span></td>
					<td id="selected_course_acad_department" />
				</tr>
				<tr class="row_space" />
				<tr>
					<td><span class="td_title"><c:out
								value="${msgs.label_acad_career}" /> </span></td>
					<td id="selected_course_acad_career" />
				</tr>
				<tr class="row_space" />
				<tr>
					<td><span class="td_title"><c:out
								value="${msgs.label_credits}" /> </span></td>
					<td id="selected_course_credits" />
				</tr>
				<tr class="row_space" />
				<tr>
					<td><span class="td_title"><c:out
								value="${msgs.label_requirements}" /> </span></td>
					<td id="selected_course_requirements" />
				</tr>
				<tr class="row_space" />
				<tr>
					<td><span class="td_title"><c:out
								value="${msgs.label_language}" /> </span></td>
					<td id="selected_course_language" />
				</tr>
			</table>
		</div>
	</div>
	<div id="div_buttons" style="clear: both;">
		<br /> <span id="save_button" class="button"><c:out
				value="${msgs.button_save}" /></span> <span id="cancel_button"
			class="button"><c:out value="${msgs.button_cancel}" /></span>
	</div>
</div>


<input type="checkbox" id="showInactives_checkbox">Afficher les descriptions inactives<br>


<table id="catalog_description_table">
	<thead>
		<tr>
			<th>Id</th>
			<th width="150px"><span><c:out value="${msgs.header_course_id}" /></span></th>
			<th><c:out value="${msgs.header_course_title}" /></th>
			<th><c:out value="${msgs.header_acad_career}" /></th>
			<th><c:out value="${msgs.header_acad_department}" /></th>
			<th width="130px"><c:out value="${msgs.header_is_description}" /></th>			
			<th width="160px"><c:out value="${msgs.header_date_modification}" /></th>
		</tr>
	</thead>
	<tbody>
	</tbody>
</table>
<input type="hidden" id="course_id" />
<input type="hidden" id="last_modified_date" />
<input type="hidden" id="genericError"
	value="<c:out value="${msgs.message_generic_error}" />" />


<jsp:directive.include file="/templates/footer.jsp" />