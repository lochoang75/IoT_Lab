//
//  Tab_Logout.swift
//  iot
//
//  Created by Le Thanh on 12/11/18.
//  Copyright © 2018 Le Thanh. All rights reserved.
//
import Foundation
import UIKit

class Tab_Logout: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
    }
    
    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */
    @IBAction func logoutUser(_ sender: Any) {
        dismiss(animated: true, completion: nil)
    }
}
