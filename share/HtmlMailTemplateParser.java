package com.sas.android.bimobile.ui.share;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;

import com.sas.android.bimobile.R;

public class HtmlMailTemplateParser {

	private static final String KEY_WORD_REPORT_SECTION_LINK = "$$REPORT_SECTION_LINK$$";
	private static final String KEY_WORD_REPORT_SECTION_UNLINK = "$$REPORT_SECTION_UNLINK$$";
	private static final String KEY_WORD_REPRT_SECTION_LINK_CONTENT = "$$REPORT_SECTION_LINK_CONTENT$$";
	private static final String KEY_WORD_REPORT_NAME = "$$REPORT_NAME$$";
	private static final String KEY_WORD_FROM = "$$FROM$$";
	private static final String KEY_WORD_USERID = "$$USERID$$";
	private static final String KEY_WORD_AUTHOR = "$$AUTHOR$$";
	private static final String KEY_WORD_LAST_MODIFIED = "$$LAST_MODIFIED$$";
	private static final String KEY_WORD_DESCRIPTION = "$$DESCRIPTION$$";
	private static final String KEY_WORD_REPORT_THUMBNAIL_SRC = "$$REPORT_THUMBNAIL_SRC$$";
	private static final String KEY_WORD_URL_TIPS = "$$REPORT_LINK_TIPS$$";
	private static final String KEY_WORD_REVIEW_TIP = "$$REVIEW_TIP$$";
	private static final String KEY_WORD_FROM_LABEL = "$$FROM_LABEL$$";
	private static final String KEY_WORD_AUTHOR_LABEL = "$$AUTHOR_LABEL$$";
	private static final String KEY_WORD_DATE_LABEL = "$$DATE_LABEL$$";
	private static final String KEY_WORD_DESCRIPTION_LABEL = "$$DESCRIPTION_LABEL$$";

	private String mHtmlTemplateContent;
	private Context mContext;
	private String mDefaultString;

	public HtmlMailTemplateParser(Context context, InputStream stream) {
		this(context, inputStream2String(stream));

	}

	public HtmlMailTemplateParser(Context context, String content) {
		this.mHtmlTemplateContent = content;
		mContext = context;
		mDefaultString = mContext.getString(R.string.mail_template_not_provided);
		replaceFixedTextLabels();

	}

	public static String inputStream2String(InputStream stream) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				stream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	private void replaceReviewTip() {
		mHtmlTemplateContent = mHtmlTemplateContent.replace(KEY_WORD_REVIEW_TIP,
				mContext.getString(R.string.mail_template_review_tip));
	}

	private void replaceFromLabel() {
		mHtmlTemplateContent = mHtmlTemplateContent.replace(KEY_WORD_FROM_LABEL,
				mContext.getString(R.string.mail_template_from_label));
	}

	private void replaceAuthorLabel() {
		mHtmlTemplateContent = mHtmlTemplateContent.replace(KEY_WORD_AUTHOR_LABEL,
				mContext.getString(R.string.mail_template_author_label));
	}

	private void replaceDateLabel() {
		mHtmlTemplateContent = mHtmlTemplateContent.replace(KEY_WORD_DATE_LABEL,
				mContext.getString(R.string.mail_template_date_label));
	}

	private void replaceDescriptionLabel(String description) {
		if (description == null || description == "")
			mHtmlTemplateContent = mHtmlTemplateContent.replace(KEY_WORD_DESCRIPTION_LABEL, "");
		else
			mHtmlTemplateContent = mHtmlTemplateContent.replace(KEY_WORD_DESCRIPTION_LABEL,
					mContext.getString(R.string.mail_template_description_label));
	}

	private void replaceFixedTextLabels() {
		replaceReviewTip();
		replaceFromLabel();
		replaceAuthorLabel();
		replaceDateLabel();
		//replaceDescriptionLabel();
	}

	private void replaceReportSectionLink(String reportSectionLink) {
		String reportSectionLinkHtml = "";
		String reportSectionUnLinkHtml = "";
		if (reportSectionLink != null) {
			reportSectionLinkHtml = mContext.getString(R.string.mail_templete_url_link,
					reportSectionLink);
			reportSectionUnLinkHtml = mContext.getString(R.string.mail_templete_url_unlink,
					reportSectionLink);
		} else {
			reportSectionLinkHtml = "";
			reportSectionUnLinkHtml = "";
		}
		mHtmlTemplateContent = mHtmlTemplateContent.replace(KEY_WORD_REPORT_SECTION_LINK,
				reportSectionLinkHtml);
		mHtmlTemplateContent = mHtmlTemplateContent.replace(KEY_WORD_REPORT_SECTION_UNLINK,
				reportSectionUnLinkHtml);
	}

	private void replaceReportSectionLinkContent(String reportSectionLinkContent) {
		if (reportSectionLinkContent == null)
			reportSectionLinkContent = "";
		mHtmlTemplateContent = mHtmlTemplateContent.replace(KEY_WORD_REPRT_SECTION_LINK_CONTENT,
				reportSectionLinkContent);
	}

	private void replaceReportName(String reportName) {
		if (reportName == null)
			reportName = mDefaultString;
		mHtmlTemplateContent = mHtmlTemplateContent.replace(KEY_WORD_REPORT_NAME, reportName);
	}

	private void replaceFrom(String from) {
		if (from == null)
			from = mContext.getString(R.string.mail_template_no_displayName);
		mHtmlTemplateContent = mHtmlTemplateContent.replace(KEY_WORD_FROM, from);
	}

	private void replaceUserID(String userID) {
		if (userID == null)
			userID = "";
		else
			userID = "(" + userID + ")";
		mHtmlTemplateContent = mHtmlTemplateContent.replace(KEY_WORD_USERID, userID);
	}

	private void replaceAuthor(String author) {
		if (author == null)
			author = mDefaultString;
		mHtmlTemplateContent = mHtmlTemplateContent.replace(KEY_WORD_AUTHOR, author);
	}

	private void replaceLastModified(String lastModified) {
		if (lastModified == null)
			lastModified = mDefaultString;
		mHtmlTemplateContent = mHtmlTemplateContent.replace(KEY_WORD_LAST_MODIFIED, lastModified);
	}

	private void replaceDiscriptionLabel(String discriptionLabel) {
		if (discriptionLabel == null || discriptionLabel.equals("")) {
			discriptionLabel = "";
		}
		replaceDescriptionLabel(discriptionLabel);
		mHtmlTemplateContent = mHtmlTemplateContent.replace(KEY_WORD_DESCRIPTION, discriptionLabel);
	}

	private void replaceReportThumbnailSrc(String reportThumbnailSrc) {
		if (reportThumbnailSrc == null)
			reportThumbnailSrc = "";
		mHtmlTemplateContent = mHtmlTemplateContent.replace(KEY_WORD_REPORT_THUMBNAIL_SRC,
				reportThumbnailSrc);
	}

	private void replaceReportURLTips(Context context, String reportURL) {
		String tips = "";
		if (reportURL != null)
			tips = context.getString(R.string.mail_template_url_tips);
		mHtmlTemplateContent = mHtmlTemplateContent.replace(KEY_WORD_URL_TIPS, tips);
	}

	public String parseHtmlTemplate(MailInfoEntity entity) {
		replaceReportSectionLink(entity.getReportSectionLink());
		replaceReportSectionLinkContent(entity.getReportSectionLink());
		replaceReportName(entity.getReportName());
		replaceFrom(entity.getFrom());
		replaceUserID(entity.getmUserID());
		replaceAuthor(entity.getAuthor());
		replaceLastModified(entity.getLastModifyDate());
		replaceDiscriptionLabel(entity.getDescription());
		replaceReportThumbnailSrc(entity.getReportThumbnailSrc());
		replaceReportURLTips(mContext, entity.getReportSectionLink());
		return mHtmlTemplateContent;
	}
}
