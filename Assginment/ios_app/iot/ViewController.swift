//
//  ViewController.swift
//  iot
//
//  Created by Le Thanh on 12/5/18.
//  Copyright © 2018 Le Thanh. All rights reserved.
//

import UIKit

class ViewController: UIViewController {

    @IBOutlet weak var txtUserName: UITextField!
    @IBOutlet weak var txtPassword: UITextField!
    @IBOutlet weak var signinButton: UIButton!
    
    override func viewDidLoad() {
        super.viewDidLoad()
    
        view.setGradientBackground(colorOne: colors.leftBackgroundColor, colorTwo: colors.rightBackgroundColor)
        
        //txtUserName.becomeFirstResponder()
        txtUserName.backgroundColor = colors.username_passwordBackground
        txtUserName.attributedPlaceholder = NSAttributedString(string: "Username", attributes: [NSAttributedString.Key.foregroundColor : UIColor.white])
        
        
        txtPassword.backgroundColor = colors.username_passwordBackground
        txtPassword.attributedPlaceholder = NSAttributedString(string: "Password", attributes: [NSAttributedString.Key.foregroundColor : UIColor.white])
        txtPassword.endEditing(true)
        signinButton.layer.cornerRadius = 35
        signinButton.backgroundColor = colors.signinBackground
    }
    
    //Function to dismiss the keyboard when tap somewhere not text view
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        view.endEditing(true)
    }
    
    
    
    
    
    @IBAction func authenticateUser(_ sender: Any) {
        if txtUserName.text == "test" && txtPassword.text == "a" {
            //let homeVC = self.storyboard?.instantiateViewController(withIdentifier: "TabBar") as! TabBar
            //self.navigationController?.pushViewController(homeVC, animated: true)
        }
        
        
    }
    @IBAction func registerUser(_ sender: Any) {
        view.endEditing(true)
    }
    

}

