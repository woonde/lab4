package com.example.sqlitelab;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private SQLHelper dbHelper;
    private LinearLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new SQLHelper(this);
        container = findViewById(R.id.container);

        Button btnAdd = findViewById(R.id.btnAdd);
        Button btnShow = findViewById(R.id.btnShow);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddDialog();
            }
        });

        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayAllOrders();
            }
        });
    }

    private void showAddDialog() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_layout, null);

        final EditText etOrderNumber = dialogView.findViewById(R.id.etOrderNumber);
        final EditText etClientName = dialogView.findViewById(R.id.etClientName);
        final EditText etDate = dialogView.findViewById(R.id.etDate);
        final EditText etCost = dialogView.findViewById(R.id.etCost);
        final EditText etStatus = dialogView.findViewById(R.id.etStatus);

        new AlertDialog.Builder(this)
                .setTitle("Новый заказ")
                .setView(dialogView)
                .setPositiveButton("Добавить", (dialog, which) -> {
                    String orderNumber = etOrderNumber.getText().toString().trim();
                    String clientName = etClientName.getText().toString().trim();
                    String date = etDate.getText().toString().trim();
                    String costStr = etCost.getText().toString().trim();
                    String status = etStatus.getText().toString().trim();

                    if (orderNumber.isEmpty() || clientName.isEmpty() || date.isEmpty()
                            || costStr.isEmpty() || status.isEmpty()) {
                        Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    double cost = Double.parseDouble(costStr);
                    long id = dbHelper.addOrder(orderNumber, clientName, date, cost, status);
                    if (id != -1) {
                        Toast.makeText(this, "Заказ добавлен с ID: " + id, Toast.LENGTH_SHORT).show();
                        displayAllOrders();
                    } else {
                        Toast.makeText(this, "Ошибка добавления", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Отмена", null)
                .show();
    }

    private void showEditDialog(final Order order) {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_layout, null);

        final EditText etOrderNumber = dialogView.findViewById(R.id.etOrderNumber);
        final EditText etClientName = dialogView.findViewById(R.id.etClientName);
        final EditText etDate = dialogView.findViewById(R.id.etDate);
        final EditText etCost = dialogView.findViewById(R.id.etCost);
        final EditText etStatus = dialogView.findViewById(R.id.etStatus);

        etOrderNumber.setText(order.getOrderNumber());
        etClientName.setText(order.getClientName());
        etDate.setText(order.getReceptionDate());
        etCost.setText(String.valueOf(order.getCost()));
        etStatus.setText(order.getStatus());

        new AlertDialog.Builder(this)
                .setTitle("Редактировать заказ")
                .setView(dialogView)
                .setPositiveButton("Сохранить", (dialog, which) -> {
                    String orderNumber = etOrderNumber.getText().toString().trim();
                    String clientName = etClientName.getText().toString().trim();
                    String date = etDate.getText().toString().trim();
                    String costStr = etCost.getText().toString().trim();
                    String status = etStatus.getText().toString().trim();

                    if (orderNumber.isEmpty() || clientName.isEmpty() || date.isEmpty()
                            || costStr.isEmpty() || status.isEmpty()) {
                        Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    order.setOrderNumber(orderNumber);
                    order.setClientName(clientName);
                    order.setReceptionDate(date);
                    order.setCost(Double.parseDouble(costStr));
                    order.setStatus(status);

                    int rows = dbHelper.updateOrder(order);
                    if (rows > 0) {
                        Toast.makeText(this, "Заказ обновлён", Toast.LENGTH_SHORT).show();
                        displayAllOrders();
                    } else {
                        Toast.makeText(this, "Ошибка обновления", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Отмена", null)
                .show();
    }

    @SuppressLint("SetTextI18n")
    private void displayAllOrders() {
        ArrayList<Order> orders = dbHelper.getAllOrders();
        container.removeAllViews();

        if (orders.isEmpty()) {
            TextView emptyView = new TextView(this);
            emptyView.setText("Список заказов пуст");
            emptyView.setTextSize(16);
            emptyView.setPadding(8, 16, 8, 8);
            container.addView(emptyView);
            return;
        }

        for (final Order order : orders) {
            TextView textView = new TextView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 0, 0, 12);
            textView.setLayoutParams(params);
            textView.setText("№" + order.getOrderNumber()
                    + " | " + order.getClientName()
                    + "\nДата: " + order.getReceptionDate()
                    + " | Стоимость: " + order.getCost() + " руб."
                    + "\nСтатус: " + order.getStatus());
            textView.setTextSize(15);
            textView.setPadding(16, 16, 16, 16);
            textView.setBackgroundResource(R.drawable.order_item_bg);

            textView.setOnClickListener(v -> showEditDialog(order));

            textView.setOnLongClickListener(v -> {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Удалить заказ?")
                        .setMessage("Удалить заказ №" + order.getOrderNumber() + "?")
                        .setPositiveButton("Да", (dialog, which) -> {
                            dbHelper.deleteOrder(order.getId());
                            Toast.makeText(MainActivity.this, "Заказ удалён", Toast.LENGTH_SHORT).show();
                            displayAllOrders();
                        })
                        .setNegativeButton("Нет", null)
                        .show();
                return true;
            });

            container.addView(textView);
        }
    }
}