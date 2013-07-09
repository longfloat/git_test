package com.sas.android.bimobile.ui.share;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.Intent;
import android.text.Html;

import com.sas.android.bimobile.R;

public class SharedURLMailTemplate extends CommonMailTemplate {

	public SharedURLMailTemplate(Context context) {
		super(context);
	}

	@Override
	public String composeText() {
		return parseHTML(mContext, mFilePath);
	}

	@Override
	public void send(final Context context) {
		Intent intent = new Intent(Intent.ACTION_SEND);

		intent.putExtra(Intent.EXTRA_SUBJECT, composeSubject());
		intent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(composeText()));
		if (mFilePath != null)
			intent.putExtra(Intent.EXTRA_STREAM, composeAttachment());
		intent.setType(composeAttachmentType());

		context.startActivity(Intent.createChooser(intent,
				context.getString(R.string.mail_template_share_via)));
	}

	public String parseHTML(Context context, String filepath) {
		String parsed = "";
		//		setReportThumbnailSrc("data:image/png;base64," + ImageUtilities.img2Base64(filepath));
		try {
			InputStream stream = context.getResources().getAssets().open(
					"template/EmailTemplate.htm");
			HtmlMailTemplateParser parser = new HtmlMailTemplateParser(context, stream);
			parsed = parser.parseHtmlTemplate(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return parsed;
	}

}
