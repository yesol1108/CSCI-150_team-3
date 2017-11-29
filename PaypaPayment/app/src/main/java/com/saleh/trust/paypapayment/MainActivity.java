package com.saleh.trust.paypapayment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import java.math.BigDecimal;

public class MainActivity extends AppCompatActivity {

    TextView m_response;
    PayPalConfiguration m_configuration;
    // the id is the link to the PayPal account, we have to create an app and get its id
    String m_paypalClientId = "AezB4yF0pekJwnuGsoP6yZF7XyEKqKyoBVuQBWtn9nI6lf6a4r_kAXtzT7jAF8AZiwRb3Uqg1vvhXHT1";
    Intent m_service;
    int m_paypalRequestCode = 999; // or any number you want

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        m_response = (TextView) findViewById(R.id.response);

        m_configuration = new PayPalConfiguration()
                .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX) // sandbox for test, production for real
                .clientId(m_paypalClientId);
        m_service = new Intent(this, PayPalService.class);
        m_service.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, m_configuration); // configuration above
        startService(m_service); // PayPal service, listening to calls to PayPal app

    }

    void pay(View view){

        PayPalPayment payment = new PayPalPayment(new BigDecimal(10), "USD", "Test payment with PayPal",
                PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, m_configuration);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
        startActivityForResult(intent, m_paypalRequestCode);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == m_paypalRequestCode){
            if(resultCode == Activity.RESULT_OK){
                // we have to confirrm that the payment worked to avoid fraud
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if(confirmation != null){
                    String state = confirmation.getProofOfPayment().getState();

                    if(state.equals("approved"))
                        m_response.setText("Payment approved");
                    else
                        m_response.setText("Error in the payment");
                }
                else
                    m_response.setText("Confirmation is null");
            }
        }
    }
}
