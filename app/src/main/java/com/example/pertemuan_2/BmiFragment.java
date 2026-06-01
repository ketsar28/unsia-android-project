package com.example.pertemuan_2;

import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Locale;

public class BmiFragment extends Fragment {

    private SeekBar sbUmur;
    private TextView tvUmurDisplay, tvBmiValue, tvBmiCategory, tvBmiRecommendation;
    private EditText etInputName, etWeight, etHeight;
    private RadioGroup rgGender;
    private CheckBox cbAgree;
    private ProgressBar pbSaving;
    private Button btnSave, btnReset, btnClearData;
    private Spinner spAktivitas;
    private TableLayout tableData;
    private MaterialCardView cvBmiRestrictedAlert, cvBmiResult;

    private int dataCount = 0;
    private String userRole = "";
    private String userName = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bmi, container, false);

        // Bind Views
        sbUmur = view.findViewById(R.id.sbUmur);
        tvUmurDisplay = view.findViewById(R.id.tvUmurDisplay);
        tvBmiValue = view.findViewById(R.id.tvBmiValue);
        tvBmiCategory = view.findViewById(R.id.tvBmiCategory);
        tvBmiRecommendation = view.findViewById(R.id.tvBmiRecommendation);
        etInputName = view.findViewById(R.id.etInputName);
        etWeight = view.findViewById(R.id.etWeight);
        etHeight = view.findViewById(R.id.etHeight);
        rgGender = view.findViewById(R.id.rgGender);
        cbAgree = view.findViewById(R.id.cbAgree);
        pbSaving = view.findViewById(R.id.pbSaving);
        btnSave = view.findViewById(R.id.btnSave);
        btnReset = view.findViewById(R.id.btnReset);
        btnClearData = view.findViewById(R.id.btnClearData);
        spAktivitas = view.findViewById(R.id.spAktivitas);
        tableData = view.findViewById(R.id.tableData);
        cvBmiRestrictedAlert = view.findViewById(R.id.cvBmiRestrictedAlert);
        cvBmiResult = view.findViewById(R.id.cvBmiResult);

        // Get role and username
        if (getActivity() != null && getActivity().getIntent() != null) {
            userRole = getActivity().getIntent().getStringExtra("EXTRA_ROLE");
            userName = getActivity().getIntent().getStringExtra("EXTRA_USERNAME");
            if (userRole == null) userRole = "";
            if (userName == null) userName = "User";
        }

        // ROLE-BASED ACCESS CONTROL
        if (userRole.equalsIgnoreCase(getString(R.string.label_role_user))) {
            // USER: Restricted access
            cvBmiRestrictedAlert.setVisibility(View.VISIBLE);
            // Lock name field to user's own name (read-only)
            etInputName.setText(userName);
            etInputName.setEnabled(false);
            etInputName.setAlpha(0.7f);
            // Disable clear data button
            btnClearData.setEnabled(false);
            btnClearData.setAlpha(0.4f);
            btnClearData.setOnClickListener(v ->
                    Toast.makeText(getContext(), getString(R.string.msg_access_denied), Toast.LENGTH_SHORT).show()
            );
        } else {
            // ADMIN: Full access
            cvBmiRestrictedAlert.setVisibility(View.GONE);
            btnClearData.setEnabled(true);
            btnClearData.setOnClickListener(v -> {
                tableData.removeAllViews();
                dataCount = 0;
                addTableHeader();
                cvBmiResult.setVisibility(View.GONE);
                Toast.makeText(getContext(), getString(R.string.data_cleared), Toast.LENGTH_SHORT).show();
            });
        }

        // Setup Spinner
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(requireContext(),
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.aktivitas_array)) {
            @Override
            public View getView(int position, View convertView, android.view.ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTextColor(ContextCompat.getColor(getContext(), R.color.text_main));
                return v;
            }

            @Override
            public View getDropDownView(int position, View convertView, android.view.ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                v.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.soft_mint));
                ((TextView) v).setTextColor(ContextCompat.getColor(getContext(), R.color.text_main));
                return v;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spAktivitas.setAdapter(adapter);

        // SeekBar
        sbUmur.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int adjustedProgress = Math.max(progress, 10);
                tvUmurDisplay.setText(getString(R.string.format_umur_display, adjustedProgress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (seekBar.getProgress() < 10) seekBar.setProgress(10);
            }
        });

        // SAVE with STRONG VALIDATION
        btnSave.setOnClickListener(v -> {
            // ---- TAHAP 1: Validasi Kolom Kosong ----
            String namaRaw = etInputName.getText().toString().trim();
            String weightStr = etWeight.getText().toString().trim();
            String heightStr = etHeight.getText().toString().trim();

            // Get TextInputLayout parents for inline error
            TextInputLayout tilName = (TextInputLayout) etInputName.getParent().getParent();
            TextInputLayout tilWeight = (TextInputLayout) etWeight.getParent().getParent();
            TextInputLayout tilHeight = (TextInputLayout) etHeight.getParent().getParent();

            boolean hasError = false;

            if (namaRaw.isEmpty()) {
                tilName.setError("Nama tidak boleh kosong!");
                hasError = true;
            } else {
                tilName.setError(null);
            }

            if (weightStr.isEmpty()) {
                tilWeight.setError("Berat badan wajib diisi!");
                hasError = true;
            } else {
                tilWeight.setError(null);
            }

            if (heightStr.isEmpty()) {
                tilHeight.setError("Tinggi badan wajib diisi!");
                hasError = true;
            } else {
                tilHeight.setError(null);
            }

            if (hasError) return;

            // ---- TAHAP 2: Validasi Rentang Medis ----
            double weight = Double.parseDouble(weightStr);
            double heightCm = Double.parseDouble(heightStr);

            if (weight < 20 || weight > 250) {
                tilWeight.setError(getString(R.string.error_weight_range));
                return;
            } else {
                tilWeight.setError(null);
            }

            if (heightCm < 100 || heightCm > 250) {
                tilHeight.setError(getString(R.string.error_height_range));
                return;
            } else {
                tilHeight.setError(null);
            }

            // ---- TAHAP 3: Verifikasi Persetujuan ----
            if (!cbAgree.isChecked()) {
                cbAgree.setTextColor(ContextCompat.getColor(requireContext(), R.color.red));
                Toast.makeText(getContext(), getString(R.string.error_agreement), Toast.LENGTH_SHORT).show();
                return;
            }
            cbAgree.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_main));

            // ---- PROSES DATA ----
            pbSaving.setVisibility(View.VISIBLE);
            btnSave.setEnabled(false);

            new Handler().postDelayed(() -> {
                if (!isAdded()) return;
                pbSaving.setVisibility(View.GONE);
                btnSave.setEnabled(true);

                double heightM = heightCm / 100.0;
                double bmi = weight / (heightM * heightM);

                String kategori = getBMICategory(bmi);
                String namaFormatted = formatTitleCase(namaRaw);
                int selectedGenderId = rgGender.getCheckedRadioButtonId();
                RadioButton rbSelected = view.findViewById(selectedGenderId);
                String gender = (rbSelected != null) ? rbSelected.getText().toString() : "-";
                int umur = Math.max(sbUmur.getProgress(), 10);

                // Show BMI Result Card
                showBmiResult(bmi, kategori);

                // Add to table
                addDataToTable(namaFormatted, gender, String.format(Locale.US, "%.1f", bmi), kategori, String.valueOf(umur));

                Toast.makeText(getContext(), getString(R.string.data_saved), Toast.LENGTH_SHORT).show();
            }, 1200);
        });

        // Reset
        btnReset.setOnClickListener(v -> {
            if (userRole.equalsIgnoreCase(getString(R.string.label_role_user))) {
                // User: only reset non-locked fields
                etWeight.setText("");
                etHeight.setText("");
            } else {
                etInputName.setText("");
                etWeight.setText("");
                etHeight.setText("");
            }
            rgGender.check(R.id.rbPria);
            cbAgree.setChecked(false);
            cbAgree.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_main));
            sbUmur.setProgress(20);
            spAktivitas.setSelection(0);
            tvUmurDisplay.setText(getString(R.string.format_umur_display, 20));
            cvBmiResult.setVisibility(View.GONE);
            // Clear errors
            TextInputLayout tilName = (TextInputLayout) etInputName.getParent().getParent();
            TextInputLayout tilWeight = (TextInputLayout) etWeight.getParent().getParent();
            TextInputLayout tilHeight = (TextInputLayout) etHeight.getParent().getParent();
            tilName.setError(null);
            tilWeight.setError(null);
            tilHeight.setError(null);
        });

        return view;
    }

    private void showBmiResult(double bmi, String kategori) {
        cvBmiResult.setVisibility(View.VISIBLE);
        tvBmiValue.setText(String.format(Locale.US, "%.1f", bmi));
        tvBmiCategory.setText(kategori);

        int bgColor, textColor;
        String recommendation;

        if (bmi < 18.5) {
            bgColor = R.color.bmi_underweight_bg;
            textColor = R.color.bmi_underweight;
            recommendation = getString(R.string.bmi_rec_kurus);
        } else if (bmi < 25) {
            bgColor = R.color.bmi_normal_bg;
            textColor = R.color.bmi_normal;
            recommendation = getString(R.string.bmi_rec_normal);
        } else if (bmi < 30) {
            bgColor = R.color.bmi_overweight_bg;
            textColor = R.color.bmi_overweight;
            recommendation = getString(R.string.bmi_rec_gemuk);
        } else {
            bgColor = R.color.bmi_obese_bg;
            textColor = R.color.bmi_obese;
            recommendation = getString(R.string.bmi_rec_obesitas);
        }

        cvBmiResult.setCardBackgroundColor(ContextCompat.getColor(requireContext(), bgColor));
        tvBmiValue.setTextColor(ContextCompat.getColor(requireContext(), textColor));
        tvBmiCategory.setTextColor(ContextCompat.getColor(requireContext(), textColor));
        tvBmiRecommendation.setText(recommendation);
    }

    private String getBMICategory(double bmi) {
        if (bmi < 18.5) return getString(R.string.kurus);
        else if (bmi < 25) return getString(R.string.normal);
        else if (bmi < 30) return getString(R.string.gemuk);
        else return getString(R.string.obesitas);
    }

    private void addTableHeader() {
        TableRow headerRow = new TableRow(getContext());
        headerRow.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.surface_green));
        headerRow.setPadding(12, 12, 12, 12);
        headerRow.addView(createTableHeaderCell(getString(R.string.header_no)));
        headerRow.addView(createTableHeaderCell(getString(R.string.header_nama)));
        headerRow.addView(createTableHeaderCell(getString(R.string.header_gender)));
        headerRow.addView(createTableHeaderCell(getString(R.string.header_bmi)));
        headerRow.addView(createTableHeaderCell(getString(R.string.header_status)));
        headerRow.addView(createTableHeaderCell(getString(R.string.header_umur)));
        tableData.addView(headerRow);
    }

    private TextView createTableHeaderCell(String text) {
        TextView tv = new TextView(getContext());
        tv.setText(text);
        tv.setTypeface(null, android.graphics.Typeface.BOLD);
        tv.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_dark_green));
        int p = (int) (8 * getResources().getDisplayMetrics().density);
        tv.setPadding(p, p, p, p);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        return tv;
    }

    private void addDataToTable(String nama, String gender, String bmi, String kategori, String umur) {
        dataCount++;
        TableRow row = new TableRow(getContext());
        row.setPadding(10, 10, 10, 10);
        if (dataCount % 2 == 0) {
            row.setBackgroundColor(android.graphics.Color.parseColor("#F5F5F5"));
        }
        row.addView(createTableCell(String.valueOf(dataCount)));
        row.addView(createTableCell(nama));
        row.addView(createTableCell(gender));
        row.addView(createTableCell(bmi));
        row.addView(createTableCell(kategori));
        row.addView(createTableCell(getString(R.string.format_umur_display, Integer.parseInt(umur))));
        tableData.addView(row);
    }

    private TextView createTableCell(String text) {
        TextView tv = new TextView(getContext());
        tv.setText(text);
        int p = (int) (8 * getResources().getDisplayMetrics().density);
        tv.setPadding(p, p, p, p);
        tv.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_main));
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        return tv;
    }

    private String formatTitleCase(String input) {
        if (input == null || input.isEmpty()) return "";
        StringBuilder titleCase = new StringBuilder();
        boolean nextTitleCase = true;
        for (char c : input.toLowerCase().toCharArray()) {
            if (Character.isSpaceChar(c)) nextTitleCase = true;
            else if (nextTitleCase) {
                c = Character.toTitleCase(c);
                nextTitleCase = false;
            }
            titleCase.append(c);
        }
        return titleCase.toString();
    }
}
