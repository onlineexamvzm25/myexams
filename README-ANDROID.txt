NMMSS ONLINE EXAM — ANDROID APK KIT

This kit adds an Android WebView wrapper around your existing live GitHub Pages site:
https://onlineexamvzm25.github.io/nmmssalur/

The app uses full-screen immersive mode (no browser address bar, status bar or navigation bar while focused), JavaScript and DOM storage. It keeps the exam site and Supabase hosted as they are. The JavaScript-generated result PDF is bridged into Android's Downloads folder.

FILES
- android/ : Android Gradle project
- .github/workflows/android-apk.yml : GitHub Actions build workflow

HOW TO ADD TO YOUR EXISTING REPOSITORY
1. Download and extract this kit ZIP.
2. Open your existing repository: https://github.com/onlineexamvzm25/nmmssalur
3. Upload the folder named `android` and the folder `.github/workflows` into the repository root. Merge the workflow file into any existing workflows; do not delete your existing workflow files.
4. Commit the changes to the default branch.
5. In GitHub, open the repository's Actions tab.
6. Select “Build NMMSS Online Exam APK”. Click “Run workflow” if it did not start automatically.
7. Wait for the green success check, open the completed run, and download the `nmmssalur-debug-apk` artifact.
8. Extract the downloaded artifact ZIP. It contains `app-debug.apk`. Transfer it to your Android phone and install it. Android may ask you to allow installs from that source.

NOTES
- The APK is a debug build for testing and direct installation, not a Play Store release. A Play Store release requires a signed release build and further checks.
- The wrapper loads the LIVE website, so changes published to GitHub Pages are reflected in the app when it reloads. An internet connection is required.
- Package ID: com.onlineexamvzm25.nmmssalur
- App label: NMMSS Online Exam
- Version: 1.0
- Screen orientation is not locked; device rotation is allowed.
- Verify login, a complete timed exam, question images, result screen and PDF save on a real device before distributing.
- Do not add Supabase service_role keys to client code. The website should only use the anon/publishable key and server-side policies/RPCs must enforce authorization.
