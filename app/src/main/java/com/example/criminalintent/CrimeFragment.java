package com.example.criminalintent;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ShareCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.io.File;
import java.net.URI;
import java.sql.Time;
import android.text.format.DateFormat;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

//314
//Задача этого фрагмента - отображение информации содержащейся в классе Crime т.е.
// выдача конкретной информации о конкретном преступлении
public class CrimeFragment extends Fragment {
    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolved;
    private Button mReportButton;
    private Button mSuspectButton;
    private Button mCallSuspectButton;
    private ImageButton mPhotoButton;
    private ImageView mPhotoView;
    private File mPhotoFile;

    //Отвечает за получение crimeID из Bundle(Сохранений)
    private static final String ARG_CRIME_ID = "crime_id";
    //Отвечает за получение date из Bundle(Сохранений)
    private static final String DIALOG_DATE = "date";
    //Отвечает за получение time из Bundle(Сохранений)
    private static final String DIALOG_TIME = "time";
    //коды возврата для даты и времени
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_TIME = 1;
    private static final int REQUEST_CONTACT = 2;
    private static final int REQUEST_CALL = 3;
    private static final int REQUEST_PHOTO = 4;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Получаем crimeId по ключуи ищем схожий объект в crimeLab
        UUID crimeId = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
        mPhotoFile = CrimeLab.get(getActivity()).getPhotoFile(mCrime);
        setHasOptionsMenu(true);
    }

    //Получает UUID, создает пакет аргументов, создает экземпляр фрагмента, а
    // затем присоединяет аргументы к фрагменту.
    public static CrimeFragment newIstance(UUID crimeId){
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, crimeId);
        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override //Именно этот метод используется для заполнения отображения layout
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_crime, container, false);
        mTitleField = view.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getmTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mCrime.setmTitle(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mDateButton = view.findViewById(R.id.crime_date);
        mDateButton.setText(mCrime.getmDate().toString());
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                DatePickerFragment dialog =  DatePickerFragment.newInstance(mCrime.getmDate());

                //Создание target фрагмента, это связь востанавливается сама, после уничтожения по коду
                //Иначе пришлось бы перегружать методы OnActivitySave и т.д.
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                dialog.show(fm, DIALOG_DATE);
            }
        });

        mDateButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                FragmentManager fm = getFragmentManager();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(mCrime.getmDate());

                int hour = calendar.get(Calendar.HOUR);
                int minute = calendar.get(Calendar.MINUTE);

                TimePickerFragment dialog = TimePickerFragment.newInstance(hour, minute);
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_TIME);
                dialog.show(fm, DIALOG_TIME);
                return true;
            }
        });


        mSolved = view.findViewById(R.id.crime_solved);
        mSolved.setChecked( mCrime.isSolved()  );
        mSolved.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mCrime.setmSolved(b);
            }
        });

        //Процесс создания неявного интента
        mReportButton = view.findViewById(R.id.crime_report);
        mReportButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                //Старая реализация интента для отправления
              /*  Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject));
                intent = Intent.createChooser(intent, getString(R.string.send_report));*/

                //Автоматическая реализация интента для отправления контакта
                startActivity(
                        ShareCompat.IntentBuilder.from(Objects.requireNonNull(getActivity()))
                        .setType("text/plain")
                        .setChooserTitle(R.string.send_report)
                        .setSubject(getString(R.string.crime_report_subject))
                        .setText(getCrimeReport()).createChooserIntent()
                );

            }

        });

        final Intent pickContact = new Intent(Intent.ACTION_PICK,
                ContactsContract.Contacts.CONTENT_URI);
        mSuspectButton = view.findViewById(R.id.crime_suspect);
        mSuspectButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                startActivityForResult(pickContact, REQUEST_CONTACT);
            }

        });

        if( mCrime.getSuspect() != null) mSuspectButton.setText(mCrime.getSuspect());

        //PackageManager известно о всех объектах установленых на устройстве
        final PackageManager packageManager = getActivity().getPackageManager();
        if(packageManager.resolveActivity(pickContact,packageManager.MATCH_DEFAULT_ONLY ) == null){
            mSuspectButton.setEnabled(false);
            Toast.makeText(getContext(), R.string.if_have_not_default_contacts,Toast.LENGTH_SHORT).show();
        }

        final Intent intent = new Intent();
        mCallSuspectButton = view.findViewById(R.id.suspect_call);
        mCallSuspectButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

            }

        });


        mPhotoButton = view.findViewById(R.id.crime_camera);
        mPhotoView = view.findViewById(R.id.crime_photo);


        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        boolean canTakePhoto = mPhotoFile != null && captureImage.resolveActivity(packageManager) != null;
        mPhotoButton.setEnabled(canTakePhoto);
        mPhotoButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Uri uri = FileProvider.getUriForFile(getActivity(), //Преобразует локальный путь в Uri
                        "com.example.criminalintent.fileprovider",
                        mPhotoFile);

                //Для того, чтобы интент не запаковал фото в низком разрешении мы сразву указываем,
                // где оно должно храниться посредством тега EXTRA_OUTPUT
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);

                List<ResolveInfo> cameraActivites =getActivity()
                        .getPackageManager()
                        .queryIntentActivities(captureImage, packageManager.MATCH_DEFAULT_ONLY);

                for (ResolveInfo activity : cameraActivites){
                    getActivity().grantUriPermission(
                            activity.activityInfo.packageName,
                            uri,
                            Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }

                startActivityForResult(captureImage, REQUEST_PHOTO);
            }
        });

        updateDateTime();
        updatePhotoView();
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode != Activity.RESULT_OK) return;

        switch (requestCode){
            //Блок установки даты посредством dialog_time
            case REQUEST_DATE:
                    Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
                    mCrime.setmDate(date);
                    updateDateTime();
            break;

                //Блок установки времени посредством dialog_time
            case REQUEST_TIME:
                    Calendar calendar =  Calendar.getInstance();
                    calendar.setTime(mCrime.getmDate());
                    calendar.set(Calendar.HOUR, (int) data.getSerializableExtra(TimePickerFragment.EXTRA_HOUR));
                    calendar.set(Calendar.MINUTE, (int) data.getSerializableExtra(TimePickerFragment.EXTRA_MINUTE));
                    mCrime.setmDate(calendar.getTime());
                    updateDateTime();
            break;

                //Получение данных из контактов устройства
            case REQUEST_CONTACT:
                if(data != null){
                    Uri contactUri = data.getData();

                    Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
                    String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;

                    String strPhoneNumber = "";

                    //Данные которые должны быть получены из контактов
                    String[] queryFields = new String[]{
                            ContactsContract.Contacts.DISPLAY_NAME,
                            ContactsContract.Contacts.HAS_PHONE_NUMBER,
                            ContactsContract.Contacts._ID
                    };

                    //Что-то на подобие цикла while, который бегает по всем контактам

                    try (Cursor contact = getActivity().getContentResolver().query(contactUri, queryFields, null, null, null)) {
                        if (contact == null) return;
                        contact.moveToFirst();

                        //Если есть номер у контакта
                        //GetContactNumber //317 можно попробовать реализовать в другой раз
                       /* if(contact.getInt(1) > 0){
                            Cursor temp = getActivity().getContentResolver().query(PhoneCONTENT_URI, null ,
                                    Phone_CONTACT_ID + " =? ", new String[]{ contact.getString(2) }, null);
                            temp.moveToFirst();

                            while (!temp.isAfterLast())
                            {
                            //    strPhoneNumber = contact.getString(contact.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            }

                        }*/

                        String suspect = contact.getString(0);
                        mCrime.setSuspect(suspect);
                        mSuspectButton.setText(getString(R.string.suspect_is, suspect));
                        Log.i("Suspect_information", suspect);
                        Log.i("Suspect_information", strPhoneNumber);
                    } catch (Exception ex){
                        ex.printStackTrace();
                    }
                }

            break;

            case REQUEST_PHOTO:
                Uri uri = FileProvider.getUriForFile(getActivity(),
                        "com.example.criminalintent.fileprovider",
                        mPhotoFile);

                getActivity().revokeUriPermission(uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                updatePhotoView();
        }

    }

    private void updateDateTime(){
        updateCrime();
        mDateButton.setText(mCrime.getmDate().toString());
    }

    private void updateCrime(){
        CrimeLab.get(getActivity()).updateCrime(mCrime);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime, menu);

        MenuItem removeFragment = menu.findItem(R.id.remove_crime);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        switch (item.getItemId()){
            case R.id.remove_crime:
                CrimeLab.get(getActivity()).remove(mCrime);

                getArguments().remove(ARG_CRIME_ID);
                getActivity().finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        CrimeLab.get(getActivity()).updateCrime(mCrime);
    }

    private String getCrimeReport(){
        String solvedStr = null;

        if(mCrime.isSolved()) solvedStr = getString(R.string.crime_report_solved);
        else solvedStr = getString(R.string.crime_report_unsolved);

        String dateFormat = "EEE, MMM dd";
        Date date = new Date(mCrime.getmDate().toString());
        String dateString = (String) DateFormat.format(dateFormat, date);

        String suspect = mCrime.getSuspect();

        suspect = (suspect == null)? getString(R.string.crime_report_no_suspect)
                                   : getString(R.string.crime_report_suspect, mCrime.getSuspect());

        String report = getString(R.string.crime_report, mCrime.getmTitle(), dateString, solvedStr, suspect);
        return report;
    }

    private void updatePhotoView(){
        if(mPhotoFile == null || !mPhotoFile.exists()){
            mPhotoView.setImageDrawable(null);
        }
        else {
            Bitmap bitmap= PictureUtils.getScaledBitmap(
                    mPhotoFile.getPath(),
                    getActivity()
            );

            mPhotoView.setImageBitmap(bitmap);
        }
    }
}
