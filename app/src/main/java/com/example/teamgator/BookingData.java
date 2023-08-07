package com.example.teamgator;

public class BookingData {
        private String name;
        private String email;
        private String date;
        private String time;
        private String contact;
        private String remarks;

        public BookingData(String name, String email, String date, String time, String contact, String remarks) {
            this.name = name;
            this.email = email;
            this.date = date;
            this.time = time;
            this.contact = contact;
            this.remarks = remarks;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getContact() {
            return contact;
        }

        public void setContact(String contact) {
            this.contact = contact;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }
    }


