//
//  getjson.swift
//  iot
//
//  Created by Le Thanh on 12/5/18.
//  Copyright © 2018 Le Thanh. All rights reserved.
//

import Foundation
struct getjson : Codable {
    struct Gateways : Codable {
        let GatewayID : Int
        struct GatewayData : Codable {
            let NodeID : Int
            struct NodeData : Codable {
                let time : Int32
                let humidnity : Int
                
            }
            let NodeData : [NodeData]
        }
        let GatewayData : [GatewayData]
    }
    let Gateways : [Gateways]

    
    static func getData (completion: @escaping (getjson) -> ()) {
        let urll = "http://192.168.64.2/backend/json.php"
        
        guard let url = URL(string: urll) else { return }
        
        URLSession.shared.dataTask(with: url) { (data, response, err) in
            guard let data = data else {return}
            var Array : getjson!
            
            do {
                let decoder = JSONDecoder()
                
                Array = try decoder.decode(getjson.self, from: data)
                
            }catch {
                print(error.localizedDescription)
            }
            
            completion(Array)
            
            }.resume()
    }
}

