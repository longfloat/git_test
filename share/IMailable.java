package com.sas.android.bimobile.ui.share;

import android.content.Context;
import android.net.Uri;

public interface IMailable {

	public abstract String composeSubject();

	public abstract String composeText();

	public abstract Uri composeAttachment();

	public abstract String composeAttachmentType();

	public abstract void send(Context context);

}
