/*
 * Copyright (C) 2013 YIXIA.COM
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.vov.vitamio.demo;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import io.vov.vitamio.LibsChecker;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.firebase.iid.FirebaseInstanceId;
import com.twilio.common.TwilioAccessManager;
import com.videocall.activities.MainActivity;
import com.videocall.activities.MakeCallActivity;

/**
 * List
 */
public class VitamioListActivity extends Activity  implements View.OnClickListener {
    private ImageView playArea;
	private ImageView cribArea;
	private ImageView videoCall;
	private ImageView cribView;

	private String playAreaUrl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (!LibsChecker.checkVitamioLibs(this))
			return;

        setContentView(R.layout.daycare_view);

		playAreaUrl = "rtmp://73.66.171.215:1935/rtmp/stream";

		playArea = (ImageView)findViewById(R.id.play_area);
		cribArea = (ImageView)findViewById(R.id.crib_area);
		videoCall = (ImageView)findViewById(R.id.video_call);
		cribView = (ImageView)findViewById(R.id.crib_view);

		playArea.setOnClickListener(this);
		cribArea.setOnClickListener(this);
		videoCall.setOnClickListener(this);
		cribView.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.play_area){
			Intent playAreaIntent = new Intent(this,VideoViewDemo.class);
			playAreaIntent.putExtra("url",playAreaUrl);
            startActivity(playAreaIntent);

		}
		else if (v.getId() == R.id.crib_area){

		}
		else if (v.getId() == R.id.video_call){

		}else if (v.getId() == R.id.crib_view){

		}
	}

//	protected List<Map<String, Object>> getData() {
//		List<Map<String, Object>> myData = new ArrayList<Map<String, Object>>();
//		addItem(myData, "MediaPlayer", new Intent(this, MediaPlayerDemo.class));
//		addItem(myData, "VideoView", new Intent(this, VideoViewDemo.class));
//		addItem(myData, "MediaMetadata", new Intent(this, MediaMetadataRetrieverDemo.class));
//		addItem(myData, "VideoSubtitle", new Intent(this, VideoSubtitleList.class));
//		addItem(myData, "VideoViewBuffer", new Intent(this, VideoViewBuffer.class));
//		addItem(myData, "Videocall", new Intent(this, MakeCallActivity.class));
//		return myData;
//	}
//
//	protected void addItem(List<Map<String, Object>> data, String name, Intent intent) {
//		Map<String, Object> temp = new HashMap<String, Object>();
//		temp.put("title", name);
//		temp.put("intent", intent);
//		data.add(temp);
//	}
//
//	@SuppressWarnings("unchecked")
//	@Override
//	protected void onListItemClick(ListView l, View v, int position, long id) {
//		Map<String, Object> map = (Map<String, Object>) l.getItemAtPosition(position);
//		Intent intent = (Intent) map.get("intent");
//		startActivity(intent);
//	}

}
