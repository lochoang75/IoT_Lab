//
//  Tab_Distributionchart.swift
//  iot
//
//  Created by Le Thanh on 12/12/18.
//  Copyright © 2018 Le Thanh. All rights reserved.
//

import UIKit
import Charts

class Tab_Distributionchart: UIViewController  {
    
    @IBOutlet weak var barChart: BarChartView!
    
    var button = GatewayBtn()
    var button2 = NodeBtn()
    
    
    var gatewayNumber : Int?
    
    var data: [getjson.Gateways] = []
    
    override func viewDidLoad() {
        super.viewDidLoad()
    
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
  
    }

    func barChartUpdate(data: [getjson.Gateways.GatewayData.NodeData]) {
    
        //var humidDistribution = [Double]()
        var humidDistribution = [Double]( repeating: 0.0, count: 5)
        for i in 0...data.count-1 {
            humidDistribution[(data[i].humidnity / 20)] += 1
        }
        
        for i in 0...4 {
            humidDistribution[i] = 100*humidDistribution[i]/Double(data.count)
            
        }
        // Basic set up of plan chart
        
        let entry1 = BarChartDataEntry(x: 0, y: humidDistribution[0])
        let entry2 = BarChartDataEntry(x: 1, y: humidDistribution[1])
        let entry3 = BarChartDataEntry(x: 2, y: humidDistribution[2])
        let entry4 = BarChartDataEntry(x: 3, y: humidDistribution[3])
        let entry5 = BarChartDataEntry(x: 4, y: humidDistribution[4])
        let dataSet = BarChartDataSet(values: [entry1, entry2, entry3, entry4, entry5], label: "Humidnity distribution")
        
        barChart.xAxis.granularityEnabled = true
        
        let formato:BarChartFormatter = BarChartFormatter()
        let xaxis:XAxis = XAxis()
        //formato.stringForValue(Double(0), axis: xaxis)
        xaxis.valueFormatter = formato
        barChart.xAxis.valueFormatter = xaxis.valueFormatter

        
        barChart.leftAxis.axisMinimum = 0
        barChart.leftAxis.axisMaximum = 100
        barChart.rightAxis.axisMinimum = 0
        barChart.rightAxis.axisMaximum = 100
        
        barChart.xAxis.labelPosition = .bottom
        let data = BarChartData(dataSets: [dataSet])
        barChart.data = data
        //barChart.chartDescription?.text = "Humidnity distributuon"
        
        // Color
        dataSet.colors = ChartColorTemplates.vordiplom()
        
        // Refresh chart with new data
        barChart.notifyDataSetChanged()
    }
    
    
    
}
@objc(BarChartFormatter)
public class BarChartFormatter: NSObject, IAxisValueFormatter{
    
    var months: [String]! = ["0-19", "20-39", "40-59", "60-79", "80-100"]
    
    
    public func stringForValue(_ value: Double, axis: AxisBase?) -> String {
        
        return months[Int(value)]
    }

}

extension Tab_Distributionchart: gatewayDelegate {
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

extension Tab_Distributionchart: nodeDelegate {
    func selectedNode(nodeNumber: Int) {
        barChartUpdate(data: self.data[self.gatewayNumber!].GatewayData[nodeNumber].NodeData)
    }
    
}
