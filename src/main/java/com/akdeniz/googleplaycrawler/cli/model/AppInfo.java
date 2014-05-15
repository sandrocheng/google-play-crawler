package com.akdeniz.googleplaycrawler.cli.model;

/**
 * Title;Package;Creator;Price;Installation Size;Number Of Downloads
 * @author sandro
 *
 */
public class AppInfo {
	private String title;
	private String packageName;
	private String creator;
	private String price;
	private String installationSize;
	private String numberOfDownloads;
	
	/**
	 * 
	 * @param title
	 * @param packageName
	 * @param creator
	 * @param price
	 * @param installationSize
	 * @param numberOfDownloads
	 */
	public AppInfo(String title, String packageName, String creator,
			String price, String installationSize, String numberOfDownloads) {
		this.title = title;
		this.packageName = packageName;
		this.creator = creator;
		this.price = price;
		this.installationSize = installationSize;
		this.numberOfDownloads = numberOfDownloads;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("title : " + this.title);
		sb.append(" ,packageName : " + this.packageName);
		sb.append(" ,creator : " + creator);
		sb.append(" ,price : " + price);
		sb.append(" ,installationSize : " + installationSize);
		sb.append(" ,numberOfDownloads : " + numberOfDownloads);
		return sb.toString();
	}



	public String getTitle() {
		return title;
	}

	public String getPackageName() {
		return packageName;
	}

	public String getCreator() {
		return creator;
	}

	public String getPrice() {
		return price;
	}

	public String getInstallationSize() {
		return installationSize;
	}

	public String getNumberOfDownloads() {
		return numberOfDownloads;
	}
}
