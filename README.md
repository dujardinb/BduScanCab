# Utilisation 


        btscan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Saisie.this, ScanCabActivity.class);
                intent.putExtra("FORMAT_CAB",getResources().getInteger(R.integer.FORMAT_QR_CODE));
                intent.putExtra("VIBRATE",1);
                startActivityForResult(intent,2);
            }
        });
        
        
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2) {
            if(resultCode == Activity.RESULT_OK){
                String result=data.getStringExtra("result");
                tvScan.setText(result);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                tvScan.setText("");
            }
        }
    }
