/*******************************************************************************
 * Copyright 2013 hellosky ye
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.hellosky.recyclingimageloader.util;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.util.LruCache;
import android.util.Log;

public class ImageMemoryCache {
	private static final String TAG = "MemoryCache";
	private LruCache<String, BitmapDrawable> mMemoryCache;
	private int limit = 10*1024;// default size

	public ImageMemoryCache() {
		setLimit(Math.round(0.15f * Runtime.getRuntime().maxMemory() / 1024));
		mMemoryCache = new LruCache<String, BitmapDrawable>(limit) {

			/**
			 * Notify the removed entry that is no longer being cached
			 */
			@Override
			protected void entryRemoved(boolean evicted, String key,
					BitmapDrawable oldValue, BitmapDrawable newValue) {
				if (RecyclingBitmapDrawable.class.isInstance(oldValue)) {
					// The removed entry is a recycling drawable, so notify it
					// that it has been removed from the memory cache
					((RecyclingBitmapDrawable) oldValue).setIsCached(false);
				}
			}

			/**
			 * Measure item size in kilobytes rather than units which is more
			 * practical for a bitmap cache
			 */
			@Override
			protected int sizeOf(String key, BitmapDrawable value) {
				final int bitmapSize = getBitmapSize(value) / 1024;
				return bitmapSize == 0 ? 1 : bitmapSize;
			}
		};
	}

	public void setLimit(int new_limit) {
		limit = new_limit;
	}

	public BitmapDrawable get(String id) {
		try {
			BitmapDrawable memValue = null;

			if (mMemoryCache != null) {
				memValue = mMemoryCache.get(id);
			}
			return memValue;
		} catch (NullPointerException ex) {
			return null;
		}
	}

	public void put(String id, BitmapDrawable value) {
		try {
			if (mMemoryCache != null) {
				if (RecyclingBitmapDrawable.class.isInstance(value)) {
					((RecyclingBitmapDrawable) value).setIsCached(true);
				}
				mMemoryCache.put(id, value);
			}
		} catch (Throwable th) {
			th.printStackTrace();
		}
	}

	public void clear() {
		if (mMemoryCache != null) {
			mMemoryCache.evictAll();
		}
	}

	public static int getBitmapSize(BitmapDrawable value) {
		Bitmap bitmap = value.getBitmap();

		if (Utils.hasHoneycombMR1()) {
			return bitmap.getByteCount();
		}
		// Pre HC-MR1
		return bitmap.getRowBytes() * bitmap.getHeight();
	}
}
