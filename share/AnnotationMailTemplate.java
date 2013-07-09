package com.sas.android.bimobile.ui.share;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;

public class AnnotationMailTemplate extends SharedURLMailTemplate {

	public AnnotationMailTemplate(Context context) {
		super(context);
	}

	@SuppressLint("SimpleDateFormat")
	public String getFullFileName() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		Date curDate = new Date(System.currentTimeMillis());
		String date = formatter.format(curDate);
		return getReportName().replaceAll(" ", "") + "_" + getSectionName().replaceAll(" ", "")
				+ "_" + date + ".png";
	}

	public String getShortFileName() {
		return getReportName().replaceAll(" ", "") + ".png";
	}
}
