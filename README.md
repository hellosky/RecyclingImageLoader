RecyclingImageLoader
====================

This project aims to provide a reusable tool for asynchronous loading, caching and displaying images.It can recycle Bitmap safely according to the principle of LRU so that it solves the OOM problem perfectly. It is originally based on Fedor Vlasov's project(https://github.com/thest1/LazyList) and has been refactored and improved since then.


<h2>Basic Usage</h2>

<pre>ImageLoader imageLoader=new ImageLoader(context);
...
imageLoader.DisplayImage(url, imageView);</pre>

Don't forget to add the following permissions to your AndroidManifest.xml:

<pre><code>&lt;uses-permission android:name="android.permission.INTERNET"/&gt;
&lt;uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/&gt;</code></pre>



Please create only one instance of ImageLoader and reuse it all around your application. This way image caching will be much more efficient.
