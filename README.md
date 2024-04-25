CustomizableView is a library that empowers you to create single resizable, rotatable, and movable view. With CustomizableView, you 
can enhance your app's user interface by allowing users to interactively manipulate a view's position, size, and orientation on the screen.

Features
Integrate a single child view with customizable attributes.
Easily adjust view's position, size, and rotation through corner buttons provided.
Enhance user experience by enabling dynamic view manipulation.
Quick and hassle-free integration into your Android projects.
Also added one custom corner to perform any action.
User can also customize actions wants to perform.

SetUp:

Step 1. Add the JitPack repository to your build file

1.gradle:
Add it in your root build.gradle at the end of repositories:

	dependencyResolutionManagement {
		repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
		repositories {
			mavenCentral()
			maven { url 'https://www.jitpack.io' }
		}
	}
Add the dependency

	dependencies {
	        implementation 'com.github.riti-garg92:CustomizableView:Latest_version'
	}

2.maven

	<repositories>
	<repository>
	<id>jitpack.io</id>
	<url>https://www.jitpack.io</url>
	</repository>
	</repositories>

Add the dependency

	<dependency>
	    <groupId>com.github.riti-garg92</groupId>
	    <artifactId>CustomizableView</artifactId>
	    <version>Tag</version>
	</dependency>

Usage:
In XML (// you can add any single view you want to customize)

	<p.ritika.customizableview.CustomizableView
	android:layout_width="300dp"
	android:layout_height="300dp">
       	 <ImageView
          android:layout_width="match_parent"
          android:layout_height="match_parent"
          android:src="@drawable/download"
          android:scaleType="fitXY"/>
  	</p.ritika.customizableview.CustomizableView>

You can also add view programmatically like:
// you can add any single view you want to customize

	ImageView imageView = new ImageView(this);
	imageView.setImageDrawable(getDrawable(R.drawable.download));
	imageView.setScaleType(ImageView.ScaleType.FIT_XY);
	LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
	imageView.setLayoutParams(params);
	CustomizableView  customizableView = new CustomizableView(this, 300, 300, 20);
	customizableView.addView(imageView);
	binding.coordinator.addView(customizableView);


Note: you can also hide views according to the requirements
settings button is added for custom requirements
