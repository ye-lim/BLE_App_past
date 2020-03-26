package com.example.blu_main_test1;


import android.app.Activity;
import android.widget.Toast;

    /**
     * Created by imaec on 2018-10-26.
     */

    public class BackPressHandler {

        // 마지막으로 뒤로가기 버튼을 눌렀던 시간 저장
        private long backKeyPressedTime = 0;
        // 첫 번째 뒤로가기 버튼을 누를때 표시
        private Toast toast;
        // 종료시킬 Activity
        private Activity activity;

        /**
         * 생성자
         * @param activity 종료시킬 Activity.
         */
        public BackPressHandler(Activity activity) {
            this.activity = activity;
        }

        /**
         * Default onBackPressed()
         * 2 seconds
         */
        public void onBackPressed() {
            if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
                backKeyPressedTime = System.currentTimeMillis();
                showGuide();
                return;
            }
            if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
                activity.finish();
                toast.cancel();
            }
        }

        /**
         * Toast 메세지 사용자 지정 onBackPressed(String msg)
         * @param msg Toast makeText()의 2번째 인자에 들어갈 text
         */
        public void onBackPressed(String msg) {
            if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
                backKeyPressedTime = System.currentTimeMillis();
                showGuide(msg);
                return;
            }
            if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
                activity.finish();
                toast.cancel();
            }
        }

        /**
         * 뒤로가기 간격 사용자 지정 onBackPressed(int time)
         * @param time 첫 번째 뒤로가기와 두 번째 뒤로가기 사이 간격, milliseconds 단위
         */
        public void onBackPressed(int time) {
            if (System.currentTimeMillis() > backKeyPressedTime + time) {
                backKeyPressedTime = System.currentTimeMillis();
                showGuide();
                return;
            }
            if (System.currentTimeMillis() <= backKeyPressedTime + time) {
                activity.finish();
                toast.cancel();
            }
        }

        /**
         * Toast 메세지 사용자 지정, 뒤로가기 간격 사용자 지정
         * onBackPressed(String msg, int time)
         * @param msg Toast makeText()의 2번째 인자에 들어갈 text
         * @param time 첫 번째 뒤로가기와 두 번째 뒤로가기 사이 간격, milliseconds 단위
         */
        public void onBackPressed(String msg, int time) {
            if (System.currentTimeMillis() > backKeyPressedTime + time) {
                backKeyPressedTime = System.currentTimeMillis();
                showGuide(msg);
                return;
            }
            if (System.currentTimeMillis() <= backKeyPressedTime + time) {
                activity.finish();
                toast.cancel();
            }
        }

        /**
         * Default showGuide()
         */
        private void showGuide() {
            toast = Toast.makeText(activity, "\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
            toast.show();
        }

        /**
         * Toast 메세지 사용자 지정 showGuide(String msg)
         * @param msg Toast makeText()의 2번째 인자에 들어갈 text
         */
        private void showGuide(String msg) {
            toast = Toast.makeText(activity, msg, Toast.LENGTH_SHORT);
            toast.show();
        }

        public void onBackPressed(boolean check) {
            if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
                backKeyPressedTime = System.currentTimeMillis();
                showGuide();
                return;
            }
            if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
                activity.moveTaskToBack(check);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
                toast.cancel();
            }
        }
}
