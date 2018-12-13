
//
//  Tab_Linechart.swift
//  iot
//
//  Created by Le Thanh on 12/12/18.
//  Copyright © 2018 Le Thanh. All rights reserved.
//

import UIKit

protocol gatewayDelegate {
    func selectedGateway(gatewayNumber : Int)
}

protocol nodeDelegate {
    func selectedNode(nodeNumber : Int)
}

protocol dateDelegate {
    func selectedDate(date : String)
}

class Tab_Home: UIViewController {
    var button = GatewayBtn()
    var button2 = NodeBtn()
    var table = nodeData()
    
    var gatewayNumber : Int?
    
    var data: [getjson.Gateways] = []
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        //Load json data
        getjson.getData(){ (results:getjson) in
            
            var i = 0
            while i < results.Gateways.count {
                self.data.append(results.Gateways[i])
                i = i + 1
            }
        }
        while data.count == 0 {
            sleep(1)
        }
        
        
        //Configure the button
        button = GatewayBtn.init(frame: CGRect(x: 0, y: 0, width: 0, height: 0))
        button.setTitle("Select Gateway", for: .normal)
        button.translatesAutoresizingMaskIntoConstraints = false
        
        //Add Button to the View Controller
        self.view.addSubview(button)
        
        
        //button Constraints
        button.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor).isActive = true
        button.leftAnchor.constraint(equalTo: view.safeAreaLayoutGuide.leftAnchor, constant: 10).isActive = true
        button.widthAnchor.constraint(equalToConstant: 180).isActive = true
        button.heightAnchor.constraint(equalToConstant: 40).isActive = true
        button.dropView.delegateTest = self
        
        //Set the drop down menu's options
        for i in 0...self.data.count-1 {
            button.dropView.dropDownOptions.append(self.data[i].GatewayID)
        }
        
        
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
}

extension Tab_Home: gatewayDelegate {
    func selectedGateway(gatewayNumber: Int) {
        self.gatewayNumber = gatewayNumber
        
        button2 = NodeBtn.init(frame: CGRect(x: 0, y: 0, width: 0, height: 0))
        button2.setTitle("Select Node", for: .normal)
        button2.translatesAutoresizingMaskIntoConstraints = false
        
        self.view.addSubview(button2)
        
        button2.topAnchor.constraint(equalTo: button.topAnchor).isActive = true
        button2.rightAnchor.constraint(equalTo: view.safeAreaLayoutGuide.rightAnchor, constant: -10).isActive = true
        button2.widthAnchor.constraint(equalToConstant: 170).isActive = true
        button2.heightAnchor.constraint(equalToConstant: 40).isActive = true
        button2.dropView.delegateTest = self
        
        for i in 0...self.data[gatewayNumber].GatewayData.count-1 {
            button2.dropView.dropDownOptions.append(self.data[gatewayNumber].GatewayData[i].NodeID)
        }
        //lbl1.text = "\(String(describing: self.gatewayNumber!))"
        
    }
    
}

extension Tab_Home: nodeDelegate {
    func selectedNode(nodeNumber: Int) {
        //Set the table behide buttons
        let adjustForTabbarInsets: UIEdgeInsets = UIEdgeInsets(top: 0, left: 0, bottom: self.tabBarController!.tabBar.frame.height, right: 0)
        table.contentInset = adjustForTabbarInsets
        table.scrollIndicatorInsets = adjustForTabbarInsets
        
        table = nodeData.init(frame: CGRect.zero, style: .grouped)
        table.translatesAutoresizingMaskIntoConstraints = false
        
        self.view.addSubview(table)
        
        table.leadingAnchor.constraint(equalTo: self.view.leadingAnchor, constant: 20).isActive = true
        table.trailingAnchor.constraint(equalTo: self.view.trailingAnchor, constant: -20).isActive = true
        table.topAnchor.constraint(equalTo: button.bottomAnchor).isActive = true
        //table.topAnchor.constraint(equalTo: self.view.safeAreaLayoutGuide.topAnchor, constant: 10).isActive = true
        table.bottomAnchor.constraint(equalTo: self.view.safeAreaLayoutGuide.bottomAnchor).isActive = true
        
        table.Nodes = self.data[self.gatewayNumber!].GatewayData[nodeNumber].NodeData
    }
    
}

protocol dropDownProtocol {
    func dropDownPressed(string : String)
    
}

class GatewayBtn: UIButton, dropDownProtocol{
    
    
    func dropDownPressed(string: String) {
        self.setTitle(string, for: .normal)
        self.dismissDropDown()
    }
    
    var dropView = dropDownView()
    
    var height = NSLayoutConstraint()
    
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        
        self.backgroundColor = UIColor.darkGray
        
        dropView = dropDownView.init(frame: CGRect.init(x: 0, y: 0, width: 0, height: 0))
        dropView.delegate = self
        
        dropView.translatesAutoresizingMaskIntoConstraints = false
    }
    
    override func didMoveToSuperview() {
        self.superview?.addSubview(dropView)
        self.superview?.bringSubviewToFront(dropView)
        dropView.topAnchor.constraint(equalTo: self.bottomAnchor).isActive = true
        dropView.centerXAnchor.constraint(equalTo: self.centerXAnchor).isActive = true
        dropView.widthAnchor.constraint(equalTo: self.widthAnchor).isActive = true
        height = dropView.heightAnchor.constraint(equalToConstant: 0)
    }
    
    var isOpen = false
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        if isOpen == false {
            
            isOpen = true
            
            NSLayoutConstraint.deactivate([self.height])
            
            if self.dropView.tableView.contentSize.height > 150 {
                self.height.constant = 150
            } else {
                self.height.constant = self.dropView.tableView.contentSize.height
            }
            
            
            NSLayoutConstraint.activate([self.height])
            
            UIView.animate(withDuration: 0.5, delay: 0, usingSpringWithDamping: 0.5, initialSpringVelocity: 0.5, options: .curveEaseInOut, animations: {
                self.dropView.layoutIfNeeded()
                self.dropView.center.y += self.dropView.frame.height / 2
            }, completion: nil)
            
        } else {
            isOpen = false
            
            NSLayoutConstraint.deactivate([self.height])
            self.height.constant = 0
            NSLayoutConstraint.activate([self.height])
            UIView.animate(withDuration: 0.5, delay: 0, usingSpringWithDamping: 0.5, initialSpringVelocity: 0.5, options: .curveEaseInOut, animations: {
                self.dropView.center.y -= self.dropView.frame.height / 2
                self.dropView.layoutIfNeeded()
            }, completion: nil)
            
        }
    }
    
    func dismissDropDown() {
        isOpen = false
        NSLayoutConstraint.deactivate([self.height])
        self.height.constant = 0
        NSLayoutConstraint.activate([self.height])
        UIView.animate(withDuration: 0.5, delay: 0, usingSpringWithDamping: 0.5, initialSpringVelocity: 0.5, options: .curveEaseInOut, animations: {
            self.dropView.center.y -= self.dropView.frame.height / 2
            self.dropView.layoutIfNeeded()
        }, completion: nil)
    }
    
    
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
}

class dropDownView: UIView, UITableViewDelegate, UITableViewDataSource  {
    
    var dropDownOptions = [Int]()
    
    var tableView = UITableView()
    
    var delegate : dropDownProtocol!
    var delegateTest: gatewayDelegate!
    override init(frame: CGRect) {
        super.init(frame: frame)
        
        tableView.backgroundColor = UIColor.darkGray
        self.backgroundColor = UIColor.darkGray
        
        
        tableView.delegate = self
        tableView.dataSource = self
        
        tableView.translatesAutoresizingMaskIntoConstraints = false
        
        self.addSubview(tableView)
        
        tableView.leftAnchor.constraint(equalTo: self.leftAnchor).isActive = true
        tableView.rightAnchor.constraint(equalTo: self.rightAnchor).isActive = true
        tableView.topAnchor.constraint(equalTo: self.topAnchor).isActive = true
        tableView.bottomAnchor.constraint(equalTo: self.bottomAnchor).isActive = true
        
    }
    
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return dropDownOptions.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = UITableViewCell()
        
        cell.textLabel?.text = "\(dropDownOptions[indexPath.row])"
        cell.backgroundColor = UIColor.darkGray
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        self.delegate.dropDownPressed(string: "Gateway selected: \(dropDownOptions[indexPath.row])")
        //self.delegateTest.show(string: "\(dropDownOptions[indexPath.row])")
        self.delegateTest.selectedGateway(gatewayNumber: indexPath.row)
        self.tableView.deselectRow(at: indexPath, animated: true)
    }
    
}


class NodeBtn: UIButton, dropDownProtocol{
    func dropDownPressed(string: String) {
        self.setTitle(string, for: .normal)
        self.dismissDropDown()
    }
    
    var dropView = dropDownView_Nodebtn()
    
    var height = NSLayoutConstraint()
    
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        
        self.backgroundColor = UIColor.darkGray
        
        dropView = dropDownView_Nodebtn.init(frame: CGRect.init(x: 0, y: 0, width: 0, height: 0))
        dropView.delegate = self
        
        dropView.translatesAutoresizingMaskIntoConstraints = false
    }
    
    override func didMoveToSuperview() {
        self.superview?.addSubview(dropView)
        self.superview?.bringSubviewToFront(dropView)
        dropView.topAnchor.constraint(equalTo: self.bottomAnchor).isActive = true
        dropView.centerXAnchor.constraint(equalTo: self.centerXAnchor).isActive = true
        dropView.widthAnchor.constraint(equalTo: self.widthAnchor).isActive = true
        height = dropView.heightAnchor.constraint(equalToConstant: 0)
    }
    
    var isOpen = false
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        if isOpen == false {
            
            isOpen = true
            
            NSLayoutConstraint.deactivate([self.height])
            
            if self.dropView.tableView.contentSize.height > 150 {
                self.height.constant = 150
            } else {
                self.height.constant = self.dropView.tableView.contentSize.height
            }
            
            
            NSLayoutConstraint.activate([self.height])
            
            UIView.animate(withDuration: 0.5, delay: 0, usingSpringWithDamping: 0.5, initialSpringVelocity: 0.5, options: .curveEaseInOut, animations: {
                self.dropView.layoutIfNeeded()
                self.dropView.center.y += self.dropView.frame.height / 2
            }, completion: nil)
            
        } else {
            isOpen = false
            
            NSLayoutConstraint.deactivate([self.height])
            self.height.constant = 0
            NSLayoutConstraint.activate([self.height])
            UIView.animate(withDuration: 0.5, delay: 0, usingSpringWithDamping: 0.5, initialSpringVelocity: 0.5, options: .curveEaseInOut, animations: {
                self.dropView.center.y -= self.dropView.frame.height / 2
                self.dropView.layoutIfNeeded()
            }, completion: nil)
            
        }
    }
    
    func dismissDropDown() {
        isOpen = false
        NSLayoutConstraint.deactivate([self.height])
        self.height.constant = 0
        NSLayoutConstraint.activate([self.height])
        UIView.animate(withDuration: 0.5, delay: 0, usingSpringWithDamping: 0.5, initialSpringVelocity: 0.5, options: .curveEaseInOut, animations: {
            self.dropView.center.y -= self.dropView.frame.height / 2
            self.dropView.layoutIfNeeded()
        }, completion: nil)
    }
    
    
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
}

class dropDownView_Nodebtn: UIView, UITableViewDelegate, UITableViewDataSource  {
    
    var dropDownOptions = [Int]()
    
    var tableView = UITableView()
    
    var delegate : dropDownProtocol!
    var delegateTest: nodeDelegate!
    override init(frame: CGRect) {
        super.init(frame: frame)
        
        tableView.backgroundColor = UIColor.darkGray
        self.backgroundColor = UIColor.darkGray
        
        
        tableView.delegate = self
        tableView.dataSource = self
        
        tableView.translatesAutoresizingMaskIntoConstraints = false
        
        self.addSubview(tableView)
        
        tableView.leftAnchor.constraint(equalTo: self.leftAnchor).isActive = true
        tableView.rightAnchor.constraint(equalTo: self.rightAnchor).isActive = true
        tableView.topAnchor.constraint(equalTo: self.topAnchor).isActive = true
        tableView.bottomAnchor.constraint(equalTo: self.bottomAnchor).isActive = true
        
    }
    
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return dropDownOptions.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = UITableViewCell()
        
        cell.textLabel?.text = "\(dropDownOptions[indexPath.row])"
        cell.backgroundColor = UIColor.darkGray
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        self.delegate.dropDownPressed(string: "Node selected: \(dropDownOptions[indexPath.row])")
        //self.delegateTest.showw(string: "\(dropDownOptions[indexPath.row])")
        self.delegateTest.selectedNode(nodeNumber: indexPath.row)
        self.tableView.deselectRow(at: indexPath, animated: true)
    }
    
}


class DateBtn: UIButton, dropDownProtocol{
    func dropDownPressed(string: String) {
        self.setTitle(string, for: .normal)
        self.dismissDropDown()
    }
    
    var dropView = dropDownView_Datebtn()
    
    var height = NSLayoutConstraint()
    
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        
        self.backgroundColor = UIColor.darkGray
        
        dropView = dropDownView_Datebtn.init(frame: CGRect.init(x: 0, y: 0, width: 0, height: 0))
        dropView.delegate = self
        
        dropView.translatesAutoresizingMaskIntoConstraints = false
    }
    
    override func didMoveToSuperview() {
        self.superview?.addSubview(dropView)
        self.superview?.bringSubviewToFront(dropView)
        dropView.topAnchor.constraint(equalTo: self.bottomAnchor).isActive = true
        dropView.centerXAnchor.constraint(equalTo: self.centerXAnchor).isActive = true
        dropView.widthAnchor.constraint(equalTo: self.widthAnchor).isActive = true
        height = dropView.heightAnchor.constraint(equalToConstant: 0)
    }
    
    var isOpen = false
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        if isOpen == false {
            
            isOpen = true
            
            NSLayoutConstraint.deactivate([self.height])
            
            if self.dropView.tableView.contentSize.height > 150 {
                self.height.constant = 150
            } else {
                self.height.constant = self.dropView.tableView.contentSize.height
            }
            
            
            NSLayoutConstraint.activate([self.height])
            
            UIView.animate(withDuration: 0.5, delay: 0, usingSpringWithDamping: 0.5, initialSpringVelocity: 0.5, options: .curveEaseInOut, animations: {
                self.dropView.layoutIfNeeded()
                self.dropView.center.y += self.dropView.frame.height / 2
            }, completion: nil)
            
        } else {
            isOpen = false
            
            NSLayoutConstraint.deactivate([self.height])
            self.height.constant = 0
            NSLayoutConstraint.activate([self.height])
            UIView.animate(withDuration: 0.5, delay: 0, usingSpringWithDamping: 0.5, initialSpringVelocity: 0.5, options: .curveEaseInOut, animations: {
                self.dropView.center.y -= self.dropView.frame.height / 2
                self.dropView.layoutIfNeeded()
            }, completion: nil)
            
        }
    }
    
    func dismissDropDown() {
        isOpen = false
        NSLayoutConstraint.deactivate([self.height])
        self.height.constant = 0
        NSLayoutConstraint.activate([self.height])
        UIView.animate(withDuration: 0.5, delay: 0, usingSpringWithDamping: 0.5, initialSpringVelocity: 0.5, options: .curveEaseInOut, animations: {
            self.dropView.center.y -= self.dropView.frame.height / 2
            self.dropView.layoutIfNeeded()
        }, completion: nil)
    }
    
    
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
}

class dropDownView_Datebtn: UIView, UITableViewDelegate, UITableViewDataSource  {
    
    var dropDownOptions = [String]()
    
    var tableView = UITableView()
    
    var delegate : dropDownProtocol!
    var delegateTest: nodeDelegate!
    var delegate3: dateDelegate!
    override init(frame: CGRect) {
        super.init(frame: frame)
        
        tableView.backgroundColor = UIColor.darkGray
        self.backgroundColor = UIColor.darkGray
        
        
        tableView.delegate = self
        tableView.dataSource = self
        
        tableView.translatesAutoresizingMaskIntoConstraints = false
        
        self.addSubview(tableView)
        
        tableView.leftAnchor.constraint(equalTo: self.leftAnchor).isActive = true
        tableView.rightAnchor.constraint(equalTo: self.rightAnchor).isActive = true
        tableView.topAnchor.constraint(equalTo: self.topAnchor).isActive = true
        tableView.bottomAnchor.constraint(equalTo: self.bottomAnchor).isActive = true
        
    }
    
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return dropDownOptions.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = UITableViewCell()
        
        cell.textLabel?.text = "\(dropDownOptions[indexPath.row])"
        cell.backgroundColor = UIColor.darkGray
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        self.delegate.dropDownPressed(string: "\(dropDownOptions[indexPath.row])")
        
        self.delegate3.selectedDate(date: dropDownOptions[indexPath.row])
        self.tableView.deselectRow(at: indexPath, animated: true)
    }
    
}





class CustomCell: UITableViewCell {
    var cellData : getjson.Gateways.GatewayData.NodeData?
    
    let indicator = UIActivityIndicatorView()
    let imageSize: CGFloat = 20
    lazy var timeImage: UIImageView = {
        let image = UIImageView()
        image.image = UIImage(named: "time")
        image.translatesAutoresizingMaskIntoConstraints = false
        //image.backgroundColor = UIColor.orange
        return image
    }()
    
    lazy var humidnityImage: UIImageView = {
        let image = UIImageView()
        image.image = UIImage(named: "humidnity")
        image.translatesAutoresizingMaskIntoConstraints = false
        //image.backgroundColor = UIColor.orange
        return image
    }()
    
    
    let labelHeight: CGFloat = 30
    
    let timeLabel: UILabel = {
        let label = UILabel()
        label.translatesAutoresizingMaskIntoConstraints = false
        label.numberOfLines = 1
        label.text = ""
        label.textColor = colors.textCorlor
        return label
    }()
    
    let humidnityLabel: UILabel = {
        let label = UILabel()
        label.translatesAutoresizingMaskIntoConstraints = false
        label.numberOfLines = 1
        label.text = ""
        label.textColor = colors.textCorlor
        return label
    }()
    
    override init(style: UITableViewCell.CellStyle, reuseIdentifier: String?) {
        super.init(style: style, reuseIdentifier: "cellID")
        //setup()
        
    }
    
    func setupLayout() {
        contentView.addSubview(timeImage)
        timeImage.leadingAnchor.constraint(equalTo: contentView.leadingAnchor).isActive = true
        timeImage.topAnchor.constraint(equalTo: contentView.topAnchor, constant: 5).isActive = true
        timeImage.heightAnchor.constraint(equalToConstant: imageSize).isActive = true
        timeImage.widthAnchor.constraint(equalToConstant: imageSize).isActive = true
        
        contentView.addSubview(timeLabel)
        timeLabel.leadingAnchor.constraint(equalTo: timeImage.trailingAnchor, constant: 5).isActive = true
        //timeLabel.trailingAnchor.constraint(equalTo: contentView.trailingAnchor, constant: -5).isActive = true
        timeLabel.topAnchor.constraint(equalTo: contentView.topAnchor, constant: 5).isActive = true
        timeLabel.bottomAnchor.constraint(equalTo: contentView.bottomAnchor, constant: -5).isActive = true
        
        
        contentView.addSubview(humidnityImage)
        humidnityImage.trailingAnchor.constraint(equalTo: contentView.trailingAnchor, constant: -70).isActive = true
        humidnityImage.topAnchor.constraint(equalTo: contentView.topAnchor, constant: 5).isActive = true
        humidnityImage.heightAnchor.constraint(equalToConstant: imageSize).isActive = true
        humidnityImage.widthAnchor.constraint(equalToConstant: imageSize).isActive = true
        
        contentView.addSubview(humidnityLabel)
        humidnityLabel.leadingAnchor.constraint(equalTo: humidnityImage.trailingAnchor, constant: 5).isActive = true
        //humidnityLabel.trailingAnchor.constraint(equalTo: contentView.trailingAnchor, constant: -5).isActive = true
        humidnityLabel.topAnchor.constraint(equalTo: contentView.topAnchor, constant: 5).isActive = true
        humidnityLabel.bottomAnchor.constraint(equalTo: contentView.bottomAnchor, constant: -5).isActive = true
    }
    
    func defaultLayout() {
        //cellImage.image = UIImage(named: "default")
        contentView.addSubview(indicator)
        indicator.translatesAutoresizingMaskIntoConstraints = false
        indicator.backgroundColor = UIColor.black.withAlphaComponent(0.3)
        NSLayoutConstraint.activate([
            indicator.leadingAnchor.constraint(equalTo: contentView.leadingAnchor),
            indicator.trailingAnchor.constraint(equalTo: contentView.trailingAnchor),
            indicator.topAnchor.constraint(equalTo: contentView.topAnchor),
            indicator.bottomAnchor.constraint(equalTo: contentView.bottomAnchor)
            ])
        indicator.startAnimating()
    }
    
    func reloadData() {
        guard let cellData = cellData else { return }
        let date = NSDate(timeIntervalSince1970: TimeInterval(cellData.time))
        let formatter = DateFormatter()
        formatter.dateFormat = "dd-MM-yyyy HH:mm:ss"
        formatter.locale = NSLocale(localeIdentifier: "en_US") as Locale!
        timeLabel.text = formatter.string(from: date as Date)
        humidnityLabel.text = "\(cellData.humidnity)%"
    }
    
    
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
}

class nodeData : UITableView, UITableViewDataSource, UITableViewDelegate {
    var Nodes = [getjson.Gateways.GatewayData.NodeData]()
    override init(frame: CGRect, style: UITableView.Style) {
        super.init(frame: CGRect.zero, style: .grouped)
        
        self.delegate = self
        self.dataSource = self
        
        self.register(CustomCell.self, forCellReuseIdentifier: "cellID")
    }
    
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return Nodes.count
    }
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "cellID", for: indexPath) as! CustomCell
        cell.setupLayout()
        //cell.defaultLayout()
        cell.cellData = Nodes[indexPath.item]
        cell.reloadData()
        //let date = NSDate(timeIntervalSince1970: TimeInterval(Nodes[indexPath.row].time)*1000)
        //let formatter = DateFormatter()
        //formatter.dateFormat = "dd-MM-yyyy HH:mm:ss"
        //formatter.locale = NSLocale(localeIdentifier: "en_US") as Locale!
        
        //cell.textLabel?.text = "    \(formatter.string(from: date as Date)) " + "           \(Nodes[indexPath.row].humidnity)%"
        return cell
    }
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
}


