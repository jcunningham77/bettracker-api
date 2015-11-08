


<%
//Get version of application
java.util.Properties prop = new java.util.Properties();
prop.load(getServletContext().getResourceAsStream("/META-INF/MANIFEST.MF"));
String build = prop.getProperty("Implementation-Version");  


%>




<HTML>
<HEAD>
<TITLE>Bet Tracker API - Test Page</TITLE>
<LINK REL="stylesheet" TYPE="text/css" HREF="index.css">

<link rel="stylesheet" href="//code.jquery.com/ui/1.11.2/themes/smoothness/jquery-ui.css">
<style media="screen" type="text/css">
	th,td {
		padding: 5px;
	}
	
	table {
		border-collapse: collapse;
		border-spacing: 0;
		border: 1px solid grey;
	}
	
	thead td {
		text-align: left;
		background-color: #0d66c9;
		color: white;
	}
	
	thead tr {
		text-align: left;
		background-color: #0d66c9;
		color: white;
	}
	
	.ui-tab-content,.ui-tabs-panel {
		/* overflow-x: scroll; */
		overflow-y: auto;
	}
</style>

<!--  <script src="//code.jquery.com/jquery-1.10.2.js"></script>-->
<script src="js/jquery-1.11.1.min.js"></script>
<script src="//code.jquery.com/ui/1.11.2/jquery-ui.js"></script>
<script>
		var x = null;
		$(function() {
			$("#parentTabs").tabs();
	
			$("#tabs").tabs({
				beforeLoad : function(event, ui) {
					ui.jqXHR.success(function(data) {
						window.x = JSON.stringify(data, null, 2);
					});
					ui.jqXHR.error(function(jqXHR, status, error) {
						// When an error occurs, the load method won't be called so forcing the display here...
						try {
							// Try to prettify JSON
							window.x = JSON.stringify(JSON.parse(jqXHR.responseText), null, 2);
						} catch(err) {
							window.x = jqXHR.responseText;
						}
						ui.panel.html('<pre>' + window.x + '</pre>');
					});
	
					return true;
				},
				load : function(event, ui) {
					ui.panel.html('<pre>' + window.x + '</pre>');
				}
			});
		});
</script>		

</HEAD>
<BODY>
	<div style="font-size: 30px" class="banner">Bet Tracker</div>
	
	<div style="font-size: 12px" class="banner">Build:  <%=build%></div>	
		
	<br>
	<div class="swagger"><a href="swagger-ui.html">Please use Swagger Documentation for V1 contracts/documentation/test harness</a></div>

	<div id="parentTabs" style="font-size: 15px">
		<ul>
			<li><a href="#divActuator">Actuator</a></li>
			
			<li><a href="#divReleaseNotes">Release Notes</a></li>
		</ul>
	
  
	
	<div id="divActuator">
		<table>
			<thead>
				<tr style="background-color: #6db33f; color: white; font-size: 20px">
					<td>Spring Boot Actuator Endpoints</td>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td>
						<div id="tabs" style="font-size: 15px; width: 1200px">
							<ul>
								<li><a href="health">Health</a></li>
								<li><a href="metrics">Metrics</a></li>
								<li><a href="mappings">Mappings</a></li>
								<li><a href="beans">Beans</a></li>
								<li><a href="trace">Trace</a></li>
								<li><a href="env">Env</a></li>
								<li><a href="dump">Dump</a></li>
							</ul>
						</div> 
					</td>
				</tr>
			</tbody>
		</table>
		</div>	 
	 
		<div id="divReleaseNotes">
		<table>
			<thead>
				<tr style="background-color: #6db33f; color: white; font-size: 20px">
					<td>Release Notes</td>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td>
						<p><span class="releaseNotesBig">1.0.1</span>&nbsp;&nbsp;&nbsp;&nbsp;<span class="releaseNotesSmall">8/25/2015</span></p>
						<p class="releaseNotes">Initial Release</p>
					</td>
				</tr>								
			</tbody>
		</table>
		</div>			
	</div><!-- end parent div  -->

      
  
	
</BODY>
</HTML>
 