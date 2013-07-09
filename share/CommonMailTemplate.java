package com.sas.android.bimobile.ui.share;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.sas.android.bimobile.R;

public class CommonMailTemplate extends MailInfoEntity implements IMailable {

	protected Context mContext;

	public CommonMailTemplate(Context context) {
		this.mContext = context;
	}

	@Override
	public String composeSubject() {
		return mContext.getString(R.string.mail_template_review, getReportName());

	}

	@Override
	public String composeText() {
		String text = "";
		if (getReportName() != null)
			text += "\n" + mContext.getString(R.string.mail_template_report_name, getReportName());
		if (getAuthor() != null)
			text += "\n" + "\n" + mContext.getString(R.string.mail_template_author, getAuthor());
		if (getLastModifyDate() != null)
			text += "\n" + mContext.getString(R.string.mail_template_date, getLastModifyDate());
		if (getDescription() != null)
			text += "\n" + mContext.getString(R.string.mail_template_description, getDescription());
		if (getReportSectionLink() != null)
			text += "\n\n\n"
					+ mContext.getString(R.string.mail_template_url, getReportName(),
							getReportSectionLink());
		return text;
	}

	@Override
	public Uri composeAttachment() {
		Uri uri = null;
		if (mFilePath != null)
			uri = Uri.parse("file:////" + mFilePath);
		return uri;
	}

	@Override
	public String composeAttachmentType() {
		return "message/rfc822";
		//		if (mFilePath != null)
		//			return "image/png";
		//		else
		//			return "text/plain";
	}

	@Override
	public void send(Context context) {
		Intent intent = new Intent(Intent.ACTION_SEND);

		intent.putExtra(Intent.EXTRA_SUBJECT, composeSubject());
		intent.putExtra(Intent.EXTRA_TEXT, composeText());
		if (mFilePath != null)
			intent.putExtra(Intent.EXTRA_STREAM, composeAttachment());
		intent.setType(composeAttachmentType());

		context.startActivity(Intent.createChooser(intent,
				context.getString(R.string.mail_template_share_via)));
	}

}
