MYEXAM education-themed Android launcher icon pack

Upload the five mipmap-* folders into:
android/app/src/main/res/

Then in android/app/src/main/AndroidManifest.xml, ensure the existing <application> tag includes:
android:icon="@mipmap/ic_launcher"
android:label="MYEXAM"

Do not replace other manifest attributes. Commit changes and rerun your Android GitHub Actions workflow.
