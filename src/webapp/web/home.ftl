<#assign s=JspTaglibs["/WEB-INF/struts-tags.tld"] />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Welcome to Interferome</title>
<#include "template/header.ftl"/>
<script>
 $(document).ready(function() {
	$('#merc').coinslider({ hoverPause: true, width: 400,height: 220, opacity: 0.3 });
});
</script>
</head>
<body>

    <#include "template/navsection.ftl"/>
    <!-- temp -->
    <div class="trans_temp">
       Interferome is currently in transition please select <a href="http://vera093.its.monash.edu.au/interferome" target="_blank">Interferome V1.0</a> to access previous version whilst this site is under development.
	</div>

    <!-- end of temp -->
	<!-- nav title -->
	<div class="nav_namebar_div nav_title_gray">
		Home
	</div>
	<div style="clear:both"></div>
 	<div class="main_container">
 		<div class="container_inner">
 			<div class="parag_top_div">
	 	 		<div class="slidshow_frame_panel">
	 	 			<div id="merc" name="merc">
						<a href="#" >
							<img src="${base}/slideshow/ifn_wordle.png" alt="Monash Institute of medical research" />
							<span>
								<b>Monash Institute of medical research</b><br />
								Interferome
							</span>
						</a>
						<a href="#" >
							<img src="${base}/slideshow/cy3.png" alt="Monash Institute of medical research" />
							<span>
								<b>Monash Institute of medical research</b><br />
								Cy3
							</span>
						</a>
						<a href="#" >
							<img src="${base}/slideshow/one_color_array.png" alt="Monash Institute of medical research" />
							<span>
								<b>Monash Institute of medical research</b><br />
								One color array
							</span>
						</a>
                        <a href="#" >
							<img src="${base}/slideshow/IFNa1.png" alt="Monash Institute of medical research" />
							<span>
								<b>Monash Institute of medical research</b><br />
								Type I, II, III
							</span>
						</a>
						<a href="#" >
                            <img src="${base}/slideshow/cy3_cy5.png" alt="Monash Institute of medical research" />
							<span>
								<b>Monash Institute of medical research</b><br />
								CY3 and CY5
							</span>
						</a>
						<a href="#" >
							<img src="${base}/slideshow/typei_ii_iii.jpg" alt="Monash Institute of medical research" />
							<span>
								<b>Monash Institute of medical research</b><br />
								Type I, II, III
							</span>
						</a>
					</div>
	 	 		</div>
	 	 		<div class="slide_right_div">
	 	 			<div class="content_title">
	 					The Database of IFN Regulated Genes
	 				</div>
			 		<div class="content_div">
						Type I, II and III IFN regulated genes manually curated from more than 28 publicly available microarray datasets.
			 		</div>
			 		<br/>
			 		<div class="content_title">
			 			Introduction
			 		</div>
			 		<div class="content_div">
						IFNs were identified as antiviral proteins more than 50 years ago. However, their involvement in immunomodulation, 
						cell proliferation, inflammation and other homeostatic process has been since identified. These cytokines are used 
						as therapeutics in many diseases such as chronic viral infections, cancer and multiple sclerosis. These IFNs regulate
						the transcription of approximately 2000 genes in a IFN subtype, dose, cell type and stimulus dependent manner. 
						This database of IFN regulated genes is an attempt at integrating information from high-throughput experiments to gain 
						a detailed understanding of IFN biology.
			 		</div>
			 		<br/>
			 		<div class="content_title">
			 			Definitions
			 		</div>
			 		<div class="content_div">
			 			Interferon Regulated Genes (IRGs) were identified from multiple microarray and proteomic experiments where cells were 
			 			treated with IFNs. Genes that were up or down regulated more than 1.5 fold relative to control samples were defined as IRGs. 
			 		</div>
	 	 		</div>
 	 		</div>
 	 		<div style="clear:both"></div>  
	 		
	 		<div class="content_title">
	 			Database Scope
	 		</div>
	 		<div class="content_div">
	 			This database will enable the reliable identification of interferon regulated gene signatures from high-throughput data sets 
	 			(i.e. microarray, proteomic data etc.). It will also assist in identifying regulatory elements and enable comparison of tissue 
	 			expression of IRGs in human and mouse. Availability of sequence information from more than 37 species, together with comprehensive 
	 			annotation will enable comparative genomics and phylogenetic analysis to be performed on these IRGs.
	 		</div>
			<div class="empty_div"></div>  	
			<div class="empty_div"></div>
 		</div>
 	</div>
 	<#include "template/footer.ftl"/>
 	<br/>
 	<br/>
</body>
</html>