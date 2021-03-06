package com.sdw.soft.common.web.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sdw.soft.common.reply.service.IReplyService;
import com.sdw.soft.core.utils.WechatAuthentication;

/**
 * @author Sonicery_D
 * @date 2014年10月28日
 * @version 1.0.0
 */
@Controller
public class LoadingController {

	private static final Logger logger = LoggerFactory.getLogger(LoadingController.class);
	
	@Autowired
	private IReplyService replyService;
	
	@RequestMapping(value="/verify",method=RequestMethod.GET)
	@ResponseBody
	public String verifyWechat(@RequestParam(value="signature")String signature,
			@RequestParam(value="timestamp") String timestamp,
			@RequestParam(value="nonce") String nonce,
			@RequestParam(value="echostr") String echostr){
		if(WechatAuthentication.authenticate(signature, timestamp, nonce)){//认证成功
			logger.info("认证成功!");
			return echostr;
		}else{//认证失败
			logger.info("认证失败!");
			return "error";
		}
	}
	
	@RequestMapping(value="/verify",method=RequestMethod.POST)
	@ResponseBody
	public String fetchMessage(HttpServletRequest request,HttpServletResponse response) throws IOException{
		response.setContentType("text/html;charset=UTF-8");
		String requestXml = IOUtils.toString(request.getInputStream(), "UTF-8");
		logger.info("from wechat's message is:{}",requestXml);
		String responseXml = null;
		responseXml = replyService.dealReceiveMessage(requestXml);
		logger.info("server reply responseXml is:{}",responseXml);
		return responseXml;
	}
	
	
	@RequestMapping(value="/index")
	public String forwardIndex(){
		return "/common/index";
	}
	
	
	
	
}
