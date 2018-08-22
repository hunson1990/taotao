package com.taotao.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import sun.net.ftp.FtpClient;

/**
 * ftp上传下载工具类
 * <p>Title: FtpUtil</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.com</p> 
 * @author	入云龙
 * @date	2015年7月29日下午8:11:51
 * @version 1.0
 */
public class FtpUtil {

	private FTPClient ftp = new FTPClient();

	public boolean connect(String addr, int port, String username, String password) {
		try {
			int reply;
			ftp.connect(addr);
			System.out.println("连接到：" + addr + ":" + port);
			System.out.print(ftp.getReplyString());
			reply = ftp.getReplyCode();

			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				System.err.println("FTP目标服务器积极拒绝.");
				System.exit(1);
				return false;
			}else{
				ftp.login(username, password);
				ftp.enterLocalPassiveMode();
				ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
				System.out.println("已连接：" + addr + ":" + port);
				return true;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(ex.getMessage());
			return false;
		}
	}

	public void createDir(String dirname){
		try{
			ftp.makeDirectory(dirname);
			System.out.println("在目标服务器上成功建立了文件夹: " + dirname);
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}

	public void changeDir(String dir){
		try {
			if(ftp.changeWorkingDirectory(dir)){
				System.out.println("切换工作目录到：" + dir + "成功！");
			}else {
				System.out.println("切换工作目录到：" + dir + "失败！");
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Boolean upload(String name, InputStream inputStream){
		try {
			//设置文件上传形式为 二进制
			ftp.setFileType(FTP.BINARY_FILE_TYPE);
			//上传文件
			ftp.storeFile(name, inputStream);
			System.out.println("文件上传成功.");
			return true;
		} catch (IOException e) {
			System.out.println("文件上传失败");
			e.printStackTrace();
		} finally {
			try {
				ftp.logout();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * Description: 向FTP服务器上传文件 
	 * @param host FTP服务器hostname 
	 * @param port FTP服务器端口 
	 * @param username FTP登录账号 
	 * @param password FTP登录密码
	 * @param filePath FTP服务器文件存放路径。例如分日期存放：/2015/01/01。文件的路径为basePath+filePath
	 * @param filename 上传到FTP服务器上的文件名 
	 * @param input 输入流 
	 * @return 成功返回true，否则返回false 
	 */  
	public boolean uploadFile(String host, int port, String username, String password,
							  String filePath, String filename, InputStream input) {

		this.connect(host, 20, username, password);
		String basePath = "/images";
		this.changeDir(basePath);
		//创建日期目录
		this.createDir(filePath);
		this.changeDir(filePath);
		if(upload(filename, input)){
			return true;
		}
		return false;

	}
	
	/** 
	 * Description: 从FTP服务器下载文件 
	 * @param host FTP服务器hostname 
	 * @param port FTP服务器端口 
	 * @param username FTP登录账号 
	 * @param password FTP登录密码 
	 * @param remotePath FTP服务器上的相对路径 
	 * @param fileName 要下载的文件名 
	 * @param localPath 下载后保存到本地的路径 
	 * @return 
	 */  
	public static boolean downloadFile(String host, int port, String username, String password, String remotePath,
			String fileName, String localPath) {
		boolean result = false;
		FTPClient ftp = new FTPClient();
		try {
			int reply;
			ftp.connect(host, port);
			// 如果采用默认端口，可以使用ftp.connect(host)的方式直接连接FTP服务器
			ftp.login(username, password);// 登录
			reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				return result;
			}
			ftp.changeWorkingDirectory(remotePath);// 转移到FTP服务器目录
			FTPFile[] fs = ftp.listFiles();
			for (FTPFile ff : fs) {
				if (ff.getName().equals(fileName)) {
					File localFile = new File(localPath + "/" + ff.getName());

					OutputStream is = new FileOutputStream(localFile);
					ftp.retrieveFile(ff.getName(), is);
					is.close();
				}
			}

			ftp.logout();
			result = true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException ioe) {
				}
			}
		}
		return result;
	}

}
