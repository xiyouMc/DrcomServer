package com.mc.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.TableRow;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.htmlparser.visitors.HtmlPage;

public class HtmlUtil {

	// 返回该标签的内容
	public static String getHtmlTag(HttpURLConnection httpURLConnection,
			String s) {
		NodeList nodes;
		String result = "error";
		try {
			Parser parser = new Parser(httpURLConnection);
			NodeFilter filter = new TagNameFilter(s);
			nodes = parser.extractAllNodesThatMatch(filter);
			if (nodes != null) {
				for (int i = 0; i < nodes.size(); i++) {
					Node textnode = (Node) nodes.elementAt(i);
					result = textnode.toPlainTextString();
				}
			}
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*System.out.println("==============fdf==========================");
		System.out.println(result);*/
		return result;
	}

	/**
	 * 获取tittle
	 */
	public static String getHtmlTittle(String html) {
		String tittle = "error";
		try {
			Parser parser = new Parser(html);
			/*
			 * Parser parser = new Parser( httpURLConnection);
			 */
			parser.setEncoding("utf8");
			HtmlPage htmlPage = new HtmlPage(parser);
			parser.visitAllNodesWith(htmlPage);
			tittle = htmlPage.getTitle();
//			System.out.println(tittle);
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("==============fdf==========================");
		System.out.println(tittle);
		return tittle;
	}

	/**
	 * 获取body
	 */
	public static String getHtmlBody(String html) {
		String body = "error";
		try {
			Parser parser = new Parser(html);
			/*
			 * Parser parser = new Parser( httpURLConnection);
			 */
			parser.setEncoding("utf8");
			HtmlPage htmlPage = new HtmlPage(parser);
			parser.visitAllNodesWith(htmlPage);
			body = htmlPage.getBody().asString();
			body = body.replaceAll("[　\\t\\n\\r\\f(&nbsp;|gt) ]+", " ");
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*System.out.println("==============fdf==========================");
		System.out.println(body);*/
		return body;
	}

	/**
	 * 写文件
	 */

	public static void writeHtml(String html) {
		String s = html;
		FileWriter fw = null;
		File f = new File(FilePathUtil.htmlPath);
		try {
			if (!f.exists()) {
				f.createNewFile();
			}
			fw = new FileWriter(f);
			BufferedWriter out = new BufferedWriter(fw);
			out.write(s, 0, s.length() - 1);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("end");
	}

	/**
	 * 解析tr
	 */
	public static String parseHtmlTR(String html, String displaymode) {
		String body = "";
		try {
			Parser parser = new Parser(html);
			/*
			 * Parser parser = new Parser( httpURLConnection);
			 */
			parser.setEncoding("utf8");
			NodeList nodeList = parser
					.extractAllNodesThatMatch(new TagNameFilter("TR"));
			System.out.println("打印个人信息");
			if (displaymode.equals("NEWS")) {// 首页
				for (int i = 10; i < 18; i++) { // for (int i = 0; i <
												// nodeList.size(); i++) { 获取首页
												// 个人信息 从第10 行 到17行
					// 获取tr
					TableRow trt = (TableRow) nodeList.elementAt(i);
					// 获取tr子节点的内容
					// System.out.println(trt.toPlainTextString());
					
					body = body + trt.toPlainTextString();
					body = body.replaceAll("[　\\t\\r\\f(&nbsp;|gt) ]+", " ");
					body = body+"\n";
				}
			}
			if (displaymode.equals("PAID")) {// 缴费记录
				for (int i = 19; i < 24; i++) { // for (int i = 0; i <
												// nodeList.size(); i++) { 获取首页
												// 个人信息 从第10 行 到17行
					// 获取tr
					TableRow trt = (TableRow) nodeList.elementAt(i);
					// 获取tr子节点的内容
//					 System.out.println(trt.toPlainTextString());
					body = body + trt.toPlainTextString()+"\n";
					body = body.replaceAll("[　\\t\\r\\f(&nbsp;|gt) ]+", " ");
					body = body+"\n";
				}
			}
			if (displaymode.equals("LD")) {// 登录记录
				for (int i = 19; i < 24; i++) { // for (int i = 0; i <
												// nodeList.size(); i++) { 获取首页
												// 个人信息 从第10 行 到17行
					// 获取tr
					TableRow trt = (TableRow) nodeList.elementAt(i);
					// 获取tr子节点的内容
					body = body + trt.toPlainTextString()+"\n";
					body = body.replaceAll("[　\\t\\r\\f(&nbsp;|gt) ]+", " ");
					body = body+"\n";
				}
			}
			if (displaymode.equals("UD")) {// 详细资料
				for (int i = 21; i < nodeList.size()-1; i++) { // for (int i = 0; i <
												// nodeList.size(); i++) { 获取首页
												// 个人信息 从第10 行 到17行
					// 获取tr
					TableRow trt = (TableRow) nodeList.elementAt(i);
					// 获取tr子节点的内容
					body = body + trt.toPlainTextString()+"\n";
					body = body.replaceAll("[　\\t\\r\\f(&nbsp;|gt) ]+", " ");
					body = body+"\n";
				}
			}
			// System.out.println(body);
			System.out.println("===================================");
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*
		 * System.out.println("==============fdf==========================");
		 * System.out.println(body);
		 */
		return body;
	}
}
