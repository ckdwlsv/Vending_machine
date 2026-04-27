package vMachine_v3.main;

import vMachine_v3.db.DBConn;
import vMachine_v3.view.MemberView;

public class VendingMain {
    public static void main(String[] args) {

        MemberView memberView = new MemberView();
        memberView.start();
        DBConn.clase();

    }
}
