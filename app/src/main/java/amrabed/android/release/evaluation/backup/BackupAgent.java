package amrabed.android.release.evaluation.backup;

import android.app.backup.BackupAgentHelper;
import android.app.backup.BackupDataInput;
import android.app.backup.BackupDataOutput;
import android.app.backup.FileBackupHelper;
import android.app.backup.SharedPreferencesBackupHelper;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import java.io.IOException;

import amrabed.android.release.evaluation.EditSection;
import amrabed.android.release.evaluation.db.Database;

/**
 * Backup agent
 *
 * @author AmrAbed
 */

public class BackupAgent extends BackupAgentHelper
{
	private static final String TAG = BackupAgent.class.getName();

	@Override
	public void onCreate()
	{
		Log.d(TAG, "OnCreate()");

		addHelper("files",
				new FileBackupHelper(this, EditSection.LIST_FILE, Database.DATABASE_NAME));
		addHelper("preferences",
				new SharedPreferencesBackupHelper(this, getPackageName() + "_preferences"));
	}

	@Override
	public void onBackup(ParcelFileDescriptor oldState, BackupDataOutput data,
						 ParcelFileDescriptor newState) throws IOException
	{
		synchronized (EditSection.fileSyncLock)
		{
			Log.d(TAG, "Performing data backup");
			super.onBackup(oldState, data, newState);
		}
	}

	@Override
	public void onRestore(BackupDataInput data, int appVersionCode,
						  ParcelFileDescriptor newState) throws IOException
	{
		synchronized (EditSection.fileSyncLock)
		{
			Log.d(TAG, "Restoring data");
			super.onRestore(data, appVersionCode, newState);
		}
	}
}