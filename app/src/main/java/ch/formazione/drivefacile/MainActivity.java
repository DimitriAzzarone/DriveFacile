package ch.formazione.drivefacile;

import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private TextView selectedFilesText;
    private Button uploadButton;
    private List<Uri> selectedUris = new ArrayList<>();

    private final ActivityResultLauncher<String[]> filePicker =
            registerForActivityResult(new ActivityResultContracts.OpenMultipleDocuments(), uris -> {
                selectedUris = uris == null ? new ArrayList<>() : new ArrayList<>(uris);
                showSelectedFiles();
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        selectedFilesText = findViewById(R.id.selectedFilesText);
        uploadButton = findViewById(R.id.uploadButton);

        findViewById(R.id.chooseFilesButton).setOnClickListener(view ->
                filePicker.launch(new String[]{"*/*"})
        );

        uploadButton.setOnClickListener(view -> shareFilesWithDrive());
    }

    private void showSelectedFiles() {
        uploadButton.setEnabled(!selectedUris.isEmpty());
        if (selectedUris.isEmpty()) {
            selectedFilesText.setText(R.string.no_files_selected);
            return;
        }

        StringBuilder names = new StringBuilder();
        for (int i = 0; i < selectedUris.size(); i++) {
            if (i > 0) names.append('\n');
            names.append(i + 1).append(". ").append(displayName(selectedUris.get(i)));
        }
        selectedFilesText.setText(names.toString());
    }

    private String displayName(Uri uri) {
        try (Cursor cursor = getContentResolver().query(
                uri, new String[]{OpenableColumns.DISPLAY_NAME}, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                if (index >= 0) return cursor.getString(index);
            }
        }
        String fallback = uri.getLastPathSegment();
        return fallback == null ? getString(R.string.unknown_file) : fallback;
    }

    private void shareFilesWithDrive() {
        if (selectedUris.isEmpty()) {
            Toast.makeText(this, R.string.choose_at_least_one, Toast.LENGTH_SHORT).show();
            return;
        }

        String action = selectedUris.size() == 1
                ? Intent.ACTION_SEND
                : Intent.ACTION_SEND_MULTIPLE;
        Intent sendIntent = new Intent(action);
        sendIntent.setType(commonMimeType());
        sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        if (selectedUris.size() == 1) {
            sendIntent.putExtra(Intent.EXTRA_STREAM, selectedUris.get(0));
        } else {
            sendIntent.putParcelableArrayListExtra(
                    Intent.EXTRA_STREAM, new ArrayList<>(selectedUris));
        }

        ClipData clipData = ClipData.newUri(
                getContentResolver(), "File da caricare", selectedUris.get(0));
        for (int i = 1; i < selectedUris.size(); i++) {
            clipData.addItem(new ClipData.Item(selectedUris.get(i)));
        }
        sendIntent.setClipData(clipData);

        startActivity(Intent.createChooser(sendIntent, getString(R.string.choose_drive)));
    }

    private String commonMimeType() {
        Set<String> types = new LinkedHashSet<>();
        for (Uri uri : selectedUris) {
            String type = getContentResolver().getType(uri);
            if (type != null) types.add(type);
        }
        return types.size() == 1 ? types.iterator().next() : "*/*";
    }
}
