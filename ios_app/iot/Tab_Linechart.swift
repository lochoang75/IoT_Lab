//
//  Tab_Linechart.swift
//  iot
//
//  Created by Le Thanh on 12/12/18.
//  Copyright © 2018 Le Thanh. All rights reserved.
//

import UIKit
import Charts

class Tab_Linechart: UIViewController {
    @IBOutlet weak var lineChart: LineChartView!
    var button = GatewayBtn()
    var button2 = NodeBtn()
    var button3 = DateBtn()
    
    var gatewayNumber : Int?
    var nodeNumber : Int?
    
    var data: [getjson.Gateways] = []
    
    override func viewDidLoad() {
        
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
    
}


extension Tab_Linechart: gatewayDelegate {
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
    }
    
}

extension Tab_Linechart: nodeDelegate {
    func selectedNode(nodeNumber: Int) {
        self.nodeNumber = nodeNumber
        
        button3 = DateBtn.init(frame: CGRect(x: 0, y: 0, width: 0, height: 0))
        button3.setTitle("Select Date", for: .normal)
        button3.translatesAutoresizingMaskIntoConstraints = false
        
        self.view.addSubview(button3)
        
        button3.topAnchor.constraint(equalTo: button.bottomAnchor, constant: 30).isActive = true
        button3.centerXAnchor.constraint(equalTo: view.safeAreaLayoutGuide.centerXAnchor).isActive = true
        button3.widthAnchor.constraint(equalToConstant: 200).isActive = true
        button3.heightAnchor.constraint(equalToConstant: 40).isActive = true
        button3.dropView.delegate3 = self
        
        
        //timeLabel.text = formatter.string(from: date as Date)
        
        button3.dropView.dropDownOptions.append(returnDateString(time: Int(self.data[gatewayNumber!].GatewayData[nodeNumber].NodeData[0].time)) )
        for i in 1...self.data[self.gatewayNumber!].GatewayData[nodeNumber].NodeData.count-1 {
            var flag = false
            for j in 0...button3.dropView.dropDownOptions.count-1 {
                if (returnDateString(time: Int(self.data[gatewayNumber!].GatewayData[nodeNumber].NodeData[i].time)) ==  button3.dropView.dropDownOptions[j]){
                    flag = true
                }
                
            }
            if (flag == false) {
                button3.dropView.dropDownOptions.append(returnDateString(time: Int(self.data[gatewayNumber!].GatewayData[nodeNumber].NodeData[i].time)))
            }
        
        }
    }
    
}

extension Tab_Linechart: dateDelegate{
    func selectedDate(date: String) {
        self.lineChart.gridBackgroundColor = UIColor.darkGray
        
        
        var humidData = [Int]()
        
        
        for i in 0...self.data[self.gatewayNumber!].GatewayData[self.nodeNumber!].NodeData.count-1 {
            if returnDateString(time: Int(self.data[self.gatewayNumber!].GatewayData[self.nodeNumber!].NodeData[i].time)) == date {
                humidData.append(self.data[self.gatewayNumber!].GatewayData[self.nodeNumber!].NodeData[i].humidnity)
            }
        }
        
        //creating an array of data entries
        var yValues : [ChartDataEntry] = [ChartDataEntry]()
        
        for i in 0 ..< humidData.count {
            yValues.append(ChartDataEntry(x: Double(i + 1), y: Double(humidData[i])))
        }
        
        let dat = LineChartData()
        let ds = LineChartDataSet(values: yValues, label: "Humidnity Line")
        
        
        dat.addDataSet(ds)
        self.lineChart.data = dat
        
        lineChart.xAxis.granularityEnabled = true
        
    }
    
    
}

func returnDateString(time: Int) -> String {
    let date = NSDate(timeIntervalSince1970: TimeInterval(time))
    let formatter = DateFormatter()
    formatter.dateFormat = "dd-MM-yyyy"
    formatter.locale = NSLocale(localeIdentifier: "en_US") as Locale!
    return formatter.string(from: date as Date)
}




