package com.lifespace.ecpay.payment.integration.verification;

import java.lang.reflect.Method;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.lifespace.ecpay.payment.integration.domain.TradeNoAioObj;
import com.lifespace.ecpay.payment.integration.ecpayOperator.PaymentVerifyBase;
import com.lifespace.ecpay.payment.integration.errorMsg.ErrorMessage;
import com.lifespace.ecpay.payment.integration.exception.EcpayException;

public class VerifyTradeNoAio extends PaymentVerifyBase{
	public VerifyTradeNoAio(){
		super();
	}
	
	public String getAPIUrl(String mode){
		Element ele = (Element)doc.getElementsByTagName("TradeNoAio").item(0);
		String url = "";
		NodeList nodeList = ele.getElementsByTagName("url");
		for(int i = 0; i < nodeList.getLength(); i++){
			ele = (Element)nodeList.item(i);
			if(ele.getAttribute("type").equalsIgnoreCase(mode)){
				url = ele.getTextContent();
				break;
			}
		}
		if(url == ""){
			throw new EcpayException(ErrorMessage.OperatingMode_ERROR);
		}
		return url;
	}
	
	public void verifyParams(TradeNoAioObj obj){
		Class<?> cls = obj.getClass();
		Method method;
		String objValue;
		Element ele = (Element)doc.getElementsByTagName("TradeNoAio").item(0);
		NodeList nodeList = ele.getElementsByTagName("param");
		for(int i = 0; i < nodeList.getLength(); i++){
			Element tmpEle = (Element)nodeList.item(i);
			try {
				method = cls.getMethod("get"+tmpEle.getAttribute("name"), null);
				objValue = method.invoke(obj, null).toString();
			} catch (Exception e) {
				throw new EcpayException(ErrorMessage.OBJ_MISSING_FIELD);
			}
			requireCheck(tmpEle.getAttribute("name"), objValue, tmpEle.getAttribute("require").toString());
			valueCheck(tmpEle.getAttribute("type"), objValue, tmpEle);
		}
	}
}
