package com.example.flashlight;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private String cameraID;
    Dialog dialog;
    public static final String APP_PREFERENCES = "UIs"; //Память номера UI
    public static final String APP_PREFERENCES_UI0 = "UI0"; // Память настроек UI0-3
    public static final String APP_PREFERENCES_UI1 = "UI1";
    public static final String APP_PREFERENCES_UI2 = "UI2";
    public static final String APP_PREFERENCES_UI3 = "UI3";
    public static final String APP_PREFERENCES_T = "T"; // Запомненая длительность мигания
    public static final String APP_PREFERENCES_DT = "DT"; // Разность времени вспышки и паузы
    private final ScheduledExecutorService schduler = Executors.newScheduledThreadPool(1);



    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); //Не выключать экран
    }

    // Мигаем строку

    public void flashString(String str, int t,int dt) throws InterruptedException {
        saveTime();//Сохраняем тайминги в настройки


        //Заводим фонарь.
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            cameraID = cameraManager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        try {
            cameraManager.setTorchMode(cameraID, false);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        TimeUnit.MILLISECONDS.sleep(4000);
        for(int i=0;i<str.length();i++){
            switch(str.charAt(i)){
                case('1'):
                try {
                    cameraManager.setTorchMode(cameraID, false);
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
                TimeUnit.MILLISECONDS.sleep(t);
                break;
                case('0'):
                try {
                    cameraManager.setTorchMode(cameraID, true);
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
                TimeUnit.MILLISECONDS.sleep(dt);
                break;
            }
        }
        try {
            cameraManager.setTorchMode(cameraID, false);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    //Вводим ключ

    public void clickLockBtn(View view) throws InterruptedException {
        String key=getString(R.string.key);
        int t=getDelay();
        int dt=getDelayD();
        flashString(key,t,dt);
    }

    //Получаем с экрана массив значений конфига

    @NonNull
    private int[] getConfig(){
        Spinner spr1 = findViewById(R.id.slot1);
        Spinner spr2 = findViewById(R.id.slot2);
        Spinner spr3 = findViewById(R.id.slot3);
        Spinner spr4 = findViewById(R.id.slot4);
        Spinner spr5 = findViewById(R.id.slot5);
        Spinner spr6 = findViewById(R.id.slot6);
        Spinner spr7 = findViewById(R.id.slot7);
        Spinner spr8 = findViewById(R.id.slot8);
        Spinner spr9 = findViewById(R.id.slot9);
        RadioGroup rg=findViewById(R.id.radioGroup);
        RadioButton rb=findViewById(rg.getCheckedRadioButtonId());
        CheckBox cb0=findViewById(R.id.checkBox0);
        CheckBox cb1=findViewById(R.id.checkBox1);
        CheckBox cb2=findViewById(R.id.checkBox2);
        CheckBox cb3=findViewById(R.id.checkBox3);
        EditText current3 = findViewById(R.id.current3);
        EditText current4 = findViewById(R.id.current4);
        int[] config = new int[16];
        switch(rb.getText().toString()){
            case ("UI0"):
                config[0]=0;
                break;
            case ("UI1"):
                config[0]=1;
                break;
            case ("UI2"):
                config[0]=2;
                break;
            case ("UI3"):
                config[0]=3;
                break;
        }
        config[1]=spr1.getSelectedItemPosition();
        config[2]=spr2.getSelectedItemPosition();
        config[3]=spr3.getSelectedItemPosition();
        config[4]=spr4.getSelectedItemPosition();
        config[5]=spr5.getSelectedItemPosition();
        config[6]=spr6.getSelectedItemPosition();
        config[7]=spr7.getSelectedItemPosition();
        config[8]=spr8.getSelectedItemPosition();
        config[9]=spr9.getSelectedItemPosition();
        if(cb0.isChecked()){
            config[10]=1;
        }else{config[10]=0;
        }
        if(cb1.isChecked()){
            config[11]=1;
        }else{config[11]=0;
        }
        if(cb2.isChecked()){
            config[12]=1;
        }else{config[12]=0;
        }
        if(cb3.isChecked()){
            config[13]=1;
        }else{config[13]=0;
        }
        //Уровни режимов.
        if(current3.toString().indexOf(".")!=-1)
            {if((Integer.parseInt(current3.getText().toString().trim()))<121)
                {config[14]=(Integer.parseInt(current3.getText().toString().trim()));
        }
            }
        if(current4.toString().indexOf(".")!=-1)
            {if((Integer.parseInt(current4.getText().toString().trim()))<121)
                {config[15]=(Integer.parseInt(current4.getText().toString().trim()));
                }
            }
        return config;
    }

    //Читаем задержку

    public int getDelay(){
        int ms;
        EditText delay=findViewById(R.id.delay);
        if(delay.toString().indexOf(".")!=-1){ms=Integer.parseInt(delay.getText().toString().trim());}
        else{ms=100;}
        return ms;
    }

    //Читаем разницу длин вспышек

    public int getDelayD(){
        int ms;
        EditText delay=findViewById(R.id.delaydelay2);
        if(delay.toString().indexOf(".")!=-1){ms=Integer.parseInt(delay.getText().toString().trim());}
        else{ms=0;}
        return ms;
    }

    // Первернутое двоичное число из десятичного

    public String getinBin(int id){
        String inbin;
        StringBuilder temp=new StringBuilder();
        while (id>0){
            temp.append(id%2);
            id=id/2;
        }
        for(int i=temp.length();i<8;i++){
            temp.append("0");
        }
        inbin=temp.toString();
        return inbin;
    }

    //Получаем бинарную строку из массива конфига.

    public String getConfigBin(@NonNull int[] config){
        String bin= "";
        int checkSum=4;
        int j=1;
        int k=0;
        StringBuilder temp=new StringBuilder();
        temp.append("0 0000 0000 110 0010 0000 110 ");
        switch (config[0]){
            case(0):
                temp.append("0000 0000 110 0000 0000 110");
                break;
            case(1):
                temp.append("0000 1100 110 0000 0000 110");
                checkSum=checkSum+48;
                break;
            case(2):
                temp.append("0000 0100 110 0000 0000 110");
                checkSum=checkSum+32;
                break;
            case(3):
                temp.append("0000 1000 110 0000 0000 110");
                checkSum=checkSum+16;
                break;
        }
        for(int i=1;i<10;i++){
            temp.append(getinBin(config[i]));
            temp.append(" 110 ");
            checkSum=checkSum+config[i];
            }
        for(int i=10;i<14;i++){
            temp.append(config[i]);
            k=k+config[i]*j;
            j=j*2;
        }

        temp.append("1000 110 ");
        for(int i=14;i<16;i++){
            temp.append(getinBin(config[i]));
            temp.append(" 110 ");
            checkSum=checkSum+config[i];
        }

        checkSum=checkSum+k+16;
        checkSum=256-checkSum%256;
        temp.append(getinBin(checkSum));
        bin=temp.toString();
        return bin;
    }

    //ПАМЯТЬ

    //Сохраняем Загружаемое в фонарь UI

    private void saveConfig(@NonNull int[] config){
        SharedPreferences uipref;
        uipref = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = uipref.edit();
        StringBuilder configSB = new StringBuilder();
        for(int i=0;i<16;i++){
            configSB.append(config[i]);
            configSB.append(",");
        }
        switch(config[0]){
            case (0):
                editor.putString(APP_PREFERENCES_UI0, configSB.toString());
                break;
            case (1):
                editor.putString(APP_PREFERENCES_UI1, configSB.toString());
                break;
            case (2):
                editor.putString(APP_PREFERENCES_UI2, configSB.toString());
                break;
            case (3):
                editor.putString(APP_PREFERENCES_UI3, configSB.toString());
                break;
        }
        editor.apply();

    }

    //Загружаем конфиг из файла настроек в выбранное радиокнопкой ui.

    private void loadConfig(int id){
        SharedPreferences uipref;
        int[] configAr;
        String configSt = "";
        uipref = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        switch (id){
            case (0):
                configSt = uipref.getString(APP_PREFERENCES_UI0, "0,2,0,0,0,4,0,0,0,0,0,0,1,1,100,120,");
                break;
            case (1):
                configSt = uipref.getString(APP_PREFERENCES_UI1, "1,1,0,0,0,4,0,0,13,0,0,1,0,1,100,120,");;
                break;
            case (2):
                configSt = uipref.getString(APP_PREFERENCES_UI2, "2,2,0,3,5,4,6,10,14,11,0,1,0,1,100,120,");
                break;
            case (3):
                configSt = uipref.getString(APP_PREFERENCES_UI3, "3,2,0,3,0,4,8,10,12,0,0,0,1,0,100,120,");;
                break;
        }
        Spinner spr1 = findViewById(R.id.slot1);
        Spinner spr2 = findViewById(R.id.slot2);
        Spinner spr3 = findViewById(R.id.slot3);
        Spinner spr4 = findViewById(R.id.slot4);
        Spinner spr5 = findViewById(R.id.slot5);
        Spinner spr6 = findViewById(R.id.slot6);
        Spinner spr7 = findViewById(R.id.slot7);
        Spinner spr8 = findViewById(R.id.slot8);
        Spinner spr9 = findViewById(R.id.slot9);
        CheckBox cb0=findViewById(R.id.checkBox0);
        CheckBox cb1=findViewById(R.id.checkBox1);
        CheckBox cb2=findViewById(R.id.checkBox2);
        CheckBox cb3=findViewById(R.id.checkBox3);
        EditText current3 = findViewById(R.id.current3);
        EditText current4 = findViewById(R.id.current4);
        configAr=stringToArray(configSt);
        spr1.setSelection(configAr[1]);
        spr2.setSelection(configAr[2]);
        spr3.setSelection(configAr[3]);
        spr4.setSelection(configAr[4]);
        spr5.setSelection(configAr[5]);
        spr6.setSelection(configAr[6]);
        spr7.setSelection(configAr[7]);
        spr8.setSelection(configAr[8]);
        spr9.setSelection(configAr[9]);
        cb0.setChecked(configAr[10] == 1);
        cb1.setChecked(configAr[11] == 1);
        cb2.setChecked(configAr[12] == 1);
        cb3.setChecked(configAr[13] == 1);
        current3.setText(Integer.toString((configAr[14])));
        current4.setText(Integer.toString((configAr[15])));
    }

    //Перевод строки в массив.

    @NonNull
    private int[] stringToArray(String config){
        int[] configAr=new int[16];
        String[] sArray = config.split(",");
        for(int i=0;i<16; i++){
            configAr[i]=Integer.parseInt(sArray[i]);
        };
        return configAr;
    }

    // Загружаем UI из памяти в спинеры и чекбоксы

    View.OnClickListener radioButtonClickListener = new View.OnClickListener() {
        @SuppressLint("NonConstantResourceId")
        @Override
        public void onClick(View v) {
            RadioButton rb = (RadioButton)v;
            switch (rb.getId()) {
                case R.id.UI0: loadConfig(0);
                    break;
                case R.id.UI1: loadConfig(1);
                    break;
                case R.id.UI2: loadConfig(2);
                    break;
                case R.id.UI3: loadConfig(3);
                    break;

                default:
                    break;
            }
        }
    };

    //Запоминаем и востанавливаем тайминги

    private void saveTime(){
        SharedPreferences uipref;
        uipref = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = uipref.edit();
        editor.putInt(APP_PREFERENCES_T, getDelay());
        editor.putInt(APP_PREFERENCES_DT, getDelayD());
        editor.apply();
    }

    private void loadTime(){
        SharedPreferences uipref;
        EditText et1 = findViewById(R.id.delay);
       EditText et2 = findViewById(R.id.delaydelay2);
        int t,dt;
        uipref = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        t=uipref.getInt(APP_PREFERENCES_T,100);
        dt=uipref.getInt(APP_PREFERENCES_DT,100);
        et1.setText(Integer.toString(t));
        et2.setText(Integer.toString(dt));
    }

    //КОНЕЦ ПАМЯТИ

    // Интерфейс.

    @Override
    protected void onResume(){
        super.onResume();
        setContentView(R.layout.activity_main);
        loadConfig(0);
        loadTime();
        RadioButton Ui0RadioButton = findViewById(R.id.UI0);
        Ui0RadioButton.setOnClickListener(radioButtonClickListener);

        RadioButton Ui1RadioButton = (RadioButton) findViewById(R.id.UI1);
        Ui1RadioButton.setOnClickListener(radioButtonClickListener);

        RadioButton Ui2RadioButton = findViewById(R.id.UI2);
        Ui2RadioButton.setOnClickListener(radioButtonClickListener);

        RadioButton Ui3RadioButton = findViewById(R.id.UI3);
        Ui3RadioButton.setOnClickListener(radioButtonClickListener);
        dialog = new Dialog(MainActivity.this);

        // Установите заголовок . Тут раньше выводилась информация о приложении.
       // dialog.setTitle("Info");
        // Передайте ссылку на разметку
        //dialog.setContentView(R.layout.info_view);
        // Найдите элемент TextView внутри вашей разметки
        // и установите ему соответствующий текст
       // TextView text = (TextView) dialog.findViewById(R.id.textInfo);
       // text.setText(R.string.info);
    }
    //Мигаем конфиг.
    public void clickBtnWrite(View view){
        int[] array = new int[16];
        String configBin;
        int t=getDelay();
        int dt=getDelayD();
        array=getConfig();
        saveConfig(array);
        configBin=getConfigBin(array);

        try {
            flashString(configBin,t,dt);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Ток в уровень

    private int ctl(int current){
        int level;
        int[] lut =new int[]{0,2,4,6,8,10,13,16,19,23,28,33,38,
                45,52,59,68,78,89,102,116,131,149,169,191,216,
                244,275,311,350,395,445,502,565,637,717,807,
                909,1024,1153,1299,1464,1650,1860,2098,2368,
                2674,3022,3418,3871,4389,4983,5670,6466,
                7395,8490,9797,11383,13365,15965,19786};
        level = 82;
        for(int i=0;i<60; i++){
            if(((5500*(lut[i+1]+12))/32768>current)&((5500*(lut[i]+12))/32768<=current)){
                level=2*i;
            }
        }
        return level;
    }

    // Уровень в ток
    private int ltc(int level){
        int current;
        int[] lut =new int[]{0,2,4,6,8,10,13,16,19,23,28,33,38,
                45,52,59,68,78,89,102,116,131,149,169,191,216,
                244,275,311,350,395,445,502,565,637,717,807,
                909,1024,1153,1299,1464,1650,1860,2098,2368,
                2674,3022,3418,3871,4389,4983,5670,6466,
                7395,8490,9797,11383,13365,15965,19786};
        if(level % 2 == 0){current= 11000*(lut[level/2]+12)/65536;}
        else current= 5500*((lut[level/2]+12)+(lut[level/2+1]+12))/65536;
        return current;
    }

}