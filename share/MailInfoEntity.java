package com.sas.android.bimobile.ui.share;

public abstract class MailInfoEntity {

	protected String mReportName;
	protected String mSectionName;
	protected String mFrom;
	protected String mUserID;
	protected String mAuthor;
	protected String mLastModifyDate;
	protected String mDescription;
	protected String mFilePath;
	protected String mReportSectionLink;
	protected String mReportThumbnailSrc;

	public String getReportName() {
		if (mReportName.indexOf("(Report)") > 0) {
			mReportName = mReportName.substring(0, mReportName.indexOf("(Report)"));
		}
		return mReportName;
	}

	public void setReportName(String reportName) {
		this.mReportName = reportName;
	}

	public String getSectionName() {
		return mSectionName;
	}

	public void setSectionName(String sectionName) {
		this.mSectionName = sectionName;
	}

	public String getAuthor() {
		return mAuthor;
	}

	public void setAuthor(String author) {
		this.mAuthor = author;
	}

	public String getLastModifyDate() {
		return mLastModifyDate;
	}

	public void setLastModifyDate(String lastModifyDate) {
		this.mLastModifyDate = lastModifyDate;
	}

	public String getDescription() {
		return mDescription;
	}

	public void setDescription(String description) {
		this.mDescription = description;
	}

	public String getFilePath() {
		return mFilePath;
	}

	public void setFilePath(String filePath) {
		this.mFilePath = filePath;
	}

	public String getFrom() {
		return mFrom;
	}

	public void setFrom(String from) {
		this.mFrom = from;
	}

	public String getmUserID() {
		return mUserID;
	}

	public void setmUserID(String mUserID) {
		this.mUserID = mUserID;
	}

	public String getReportSectionLink() {
		return mReportSectionLink;
	}

	public void setReportSectionLink(String reportSectionLink) {
		this.mReportSectionLink = reportSectionLink;
	}

	public String getReportThumbnailSrc() {
		return mReportThumbnailSrc;
	}

	public void setReportThumbnailSrc(String reportThumbnailSrc) {
		this.mReportThumbnailSrc = reportThumbnailSrc;
	}
}
