var searchRowCount=0;var searchBlock=null;
jQuery(document).ready(function(){
	searchBlock=jQuery('#trackShpBlock');
	jQuery(searchBlock).on('keypress','#docNumber',function(event){
		if(event.which==13&&!event.shiftKey){jQuery("#trackButton").click();
	}});
	jQuery('.dtNumber').bind('input propertychange',function(){
		var isValid=jQuery.isNumeric(jQuery(this).val());
		displayErrorMessage(isValid,jQuery(this),"Numbers only allowed");
	});
	var params=getUrlParams();
	if(params&&params.docNumber){
		jQuery('#docNumber').val(params.docNumber);
		jQuery('#docType').val(params.docType);
		jQuery('#docPrefix').val(params.docPrefix);
		jQuery("#trackButton").click();}
initAddDelRow();
refreshKaptcha();
initRefreshKaptcha();
checkSplCharacter();
loadSearchHistory();
initSearchHistory();
searchHistoryMobile();
});
function initRefreshKaptcha(){
	jQuery('.loginCaptchaRe').on('click',function(){
		refreshKaptcha();}
		)}
function refreshKaptcha(){
	$.ajax({type:'POST',
		contentType:'application/json',
		url:'getKaptchaImage?operation='+'TrackShipments'}).done(function(data){
			log.debug(this.response,typeof this.response);
			jQuery('#divCaptchaImage').html(data);
		});
	}
function initAddDelRow(){
	jQuery(document).on('click','.addTrackShp',function(){
	displayErrorMessage(true,'','');
	if(searchRowCount<=8){
		searchRowCount++;
		var cloneItem=searchBlock.find('.row:first').clone(true,true);
		cloneItem.find('.iconBlock').append('<span class="tsp10 cursorPointer glyphicon glyphicon-minus-sign delTrackShp" title="click to remove this form row"></span>');
		cloneItem.find('h6').addClass('mobile-only');
		cloneItem.find('input, select').addClass('tsp5');
		cloneItem.find('.icon157').addClass('tsp5 tsp5fix767');
		cloneItem.find('.iconBlock h6').hide();
		cloneItem.find('#docNumber').val("");
		cloneItem.find('#docPrefix').val("157");
		var validatAttr1=(cloneItem.find('#docPrefix').attr('Validation-msg'))+" in row "+(searchRowCount+1);
		cloneItem.find('#docPrefix').attr('Validation-msg',validatAttr1);
		var validatAttr=(cloneItem.find('#docNumber').attr('Validation-msg'))+" in row "+(searchRowCount+1);cloneItem.find('#docNumber').attr('Validation-msg',validatAttr);cloneItem.appendTo(searchBlock);}
else{jQuery('.msgDiv').appendTo('#trackShpBlock');
jQuery('.msgDiv').html('<span class="msgTxt fltLft">Maximum 10 rows are allowed.</span> <span class="rsp10 lsp10 fltRgt cursorPointer glyphicon glyphicon-remove msgRemove"></span>').show();initMsgRemove();}});jQuery(document).on('click','.delTrackShp',function(){displayErrorMessage(true,'','');searchRowCount--;jQuery(this).parent().parent().remove();});}
function loadShipments(data){
	jQuery('#trackShipmentResult').html(data);setWidthTrackShipment();saveSearchPreferance();
}
function setHeightTrackShipment(){
	if(jQuery(window).width()>767){jQuery('.graphicalStatus').each(function(){
		var reqHeight=jQuery(this).height()-63;
		jQuery(this).find('.shpStatusDetails').css('min-height',reqHeight);
	});
};
}
function setWidthTrackShipment(){
	if(jQuery(window).width()>767){
		jQuery('.graphicalStatus').each(function(){
			var contents=jQuery(this).html();
			jQuery(this).html("<div class='graphicalWrapper'>"+contents+" </div>");
			jQuery('.graphicalWrapper').width(jQuery(this).width());
			var reqHeight=jQuery(this).find('.graphicalWrapper').height();
			jQuery(this).find('.graphicalStatus-box').css('min-height',reqHeight);});};}
jQuery(window).resize(function(){
	jQuery('.graphicalWrapper').width(jQuery('.graphicalWrapper').parent().width());
});jQuery("#clear").click(function(){searchBlock.find('#docNumber').val("")
searchBlock.find('#docPrefix').val("")
searchBlock.find('select').val("MAWB");
});
function getTrackingRequestList(){
	var cargoTrackingRequestSOs=[];
	searchBlock.children().each(function(index,value){v
		ar cargoTrackingRequestSO={};
		if(jQuery(this).find('select').val()){
			cargoTrackingRequestSO.documentType=jQuery(this).find('select').val();
			cargoTrackingRequestSO.documentPrefix=jQuery(this).find('#docPrefix').val();
			cargoTrackingRequestSO.documentNumber=jQuery(this).find('#docNumber').val();
			cargoTrackingRequestSOs.push(cargoTrackingRequestSO);}});
	var cargoTrackingRequestListSO={};cargoTrackingRequestListSO.cargoTrackingRequestSOs=cargoTrackingRequestSOs;
	return cargoTrackingRequestListSO;
}
function isCheckdtNumber(isValid){
	jQuery("#trackShpBlock").find('.dtNumber').map(function(){
		var dtNumber=jQuery(this).val();
		var isValid1=jQuery.isNumeric(dtNumber);
		if(isValid1==false){
			isValid=false;
			displayErrorMessage(isValid,jQuery(this),"Numbers only allowed");
		}
	});
	return isValid;
}
jQuery("#trackButton").click(function(){jQuery('#trackShipmentResult').html('');jQuery('#emailResult').html('');
	var isValid=doPanelMandatoryCheck(searchBlock);
	if(isValid){
		isValid=isCheckdtNumber(isValid);
		if(isValid){var trackingRequestList=getTrackingRequestList();
			var dataToSend=JSON.stringify(trackingRequestList);
			log.debug((dataToSend));
			jQuery.ajax({type:'POST',contentType:"application/json; charset=utf-8"
				,url:'doTrackShipmentsAction',
				data:dataToSend,
				success:function(responsedata){loadShipments(responsedata);}});}}});
jQuery("#downloadShpReportBtn").click(function(){
	var isValid=doPanelMandatoryCheck(searchBlock)&&doPanelMandatoryCheck('#captchaPanel');
	if(isValid){isValid=isCheckdtNumber(isValid);
		if(isValid){validateCaptcha();}}});
function validateCaptcha(){jQuery('#emailResult').html('');
var flightData={encryptedCaptchaText:jQuery('#encryptedCaptchaText').val(),operation:'TrackShipments',txtTwoWord:
jQuery('#txtTwoWord').val()};
jQuery.ajax({
	type:'POST',
	contentType:'application/json',
	url:'validateCaptcha',
	data:JSON.stringify(flightData)
}).done(function(data){
	if(data.indexOf("Invalid captcha")>-1)
{jQuery('#emailResult').html(data);refreshKaptcha();jQuery('#txtTwoWord').val('');}
else
{var trackingRequestList=getTrackingRequestList();
	var frm=document.createElement("form");
	frm.method="POST";
	frm.action='downloadShipmentReport';
	var obj=trackingRequestList.cargoTrackingRequestSOs;
	for(i=0;i<obj.length;i++){
		jQuery.each(obj[i],function(key,val){
			var cmp=document.createElement("input");
			var tmpName="cargoTrackingRequestSOs["+i+"]."+key;
			cmp.setAttribute("name",tmpName);
			cmp.setAttribute("value",val);
			cmp.setAttribute("type",'hidden');
			frm.appendChild(cmp);
		});
	}
document.body.appendChild(frm);frm.submit();
jQuery('#emailResult').html('<div class="alert alert-success">The report is getting generated now. Please wait.</div>');
refreshKaptcha();jQuery('#txtTwoWord').val('');}});};
function validateEmailField(){
	var emailId=$.trim($('#emailIds').val());
	if(emailId.length>0){return validateEmail(emailId.split(','),'#emailResult');}
return false;};
jQuery("#emailShpInfoBtn").click(function(){
	jQuery('#emailResult').html('');
	var isValid=doPanelMandatoryCheck(searchBlock)&&doPanelMandatoryCheck('#emailDownloadPanel')&&validateEmailField();
	if(isValid){isValid=isCheckdtNumber(isValid);
		if(isValid){var trackingRequestList=getTrackingRequestList();
			var listofEmailIds=[];var emailStr=$('#emailIds').val().split(",");
			for(var i=0;i<emailStr.length;i++){listofEmailIds.push(emailStr[i]);}
trackingRequestList.emailIds=listofEmailIds;var txtTwoWord=jQuery('#txtTwoWord').val();
var encryptedCaptchaText=jQuery('#encryptedCaptchaText').val();
trackingRequestList.txtTwoWord=txtTwoWord;
trackingRequestList.encryptedCaptchaText=encryptedCaptchaText;
var dataToSend=JSON.stringify(trackingRequestList);
log.debug((dataToSend));
jQuery.ajax({
	type:'POST',
	contentType:"application/json; charset=utf-8",
	url:'emailShipmentInfo',
	data:dataToSend}).done(function(data){
		jQuery('#emailResult').html('');
		jQuery('#emailResult').html(data);refreshKaptcha();
		jQuery('#txtTwoWord').val('');});}}});
function initSearchHistory(){
	jQuery(document).on('click','.seacrhHistBtn',function(){
		var awbNumSplit=jQuery(this).find('.docNumberBtn').text().replace(/\s/g,'').split("-");prefix=awbNumSplit[0];
		awb=awbNumSplit[1];jQuery("#trackShpBlock #docPrefix").val(prefix);
		jQuery("#trackShpBlock #docNumber").val(awb);
		jQuery("#trackButton").click();
	})
}
function saveSearchPreferance(){
	if(typeof window.localStorage!=="undefined"&&window.localStorage!==null){
		jQuery('#PreviousSearch').show();
		jQuery('.trackingResult').each(function(){
			var searchedDocType=jQuery(this).find('.docType').text();
			var searchedAWB=jQuery(this).find('.docNumber').text();
			var segment=jQuery(this).find('.segment').text();
			var stack=[];
			var stack=JSON.parse(localStorage.getItem("awbNumberList"))||[];
			var newEntry={type:searchedDocType,number:searchedAWB,seg:segment};
			var stack=jQuery.grep(stack,function(e){
				return(e.number!==newEntry.number);
			});
			stack.push(newEntry);if(stack.length>5){stack.shift();
			}
localStorage.setItem("awbNumberList",JSON.stringify(stack));
jQuery('#searchHistoryContainer').empty();
jQuery.each(stack,function(index){
	var awbNumber=stack[index].number;
	var origDest=stack[index].seg;
	jQuery('#searchHistoryContainer').prepend("<button class='btn btn-default padding5 seacrhHistBtn'><span class='docNumberBtn'>"+awbNumber+"</span><br/><span> "+origDest+"</span></button>");
});
});
	}else{
		jQuery('#searchHistoryContainer, #PreviousSearch').hide();
	}}
function loadSearchHistory(){
	if(typeof window.localStorage!=="undefined"&&window.localStorage!==null){
		var awbNumberList=JSON.parse(localStorage.getItem("awbNumberList"))||[];
		jQuery.each(awbNumberList,function(index){
			var awbNumber=awbNumberList[index].number;var origDest=awbNumberList[index].seg;
			jQuery('#searchHistoryContainer').prepend("<button class='btn btn-default padding5 seacrhHistBtn'><span class='docNumberBtn'>"+awbNumber+"</span><br/><span> "+origDest+"</span> </button>");
			jQuery('#PreviousSearch').show();
		});
	}else{
	jQuery('#searchHistoryContainer, #PreviousSearch').hide();
}}
function searchHistoryMobile(){
	if(jQuery(window).width()<500){
		jQuery('#PreviousSearch').click(function(){
			jQuery(this).next().slideToggle();
			jQuery(this).find('.glyphicon-chevron-up').toggleClass('glyphicon-chevron-down');
		})
	}
}