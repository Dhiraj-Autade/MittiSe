import requests
import json
from datetime import datetime

def test_real_government_api():
    """Test the real Indian Government API for mandi prices"""
    
    # Real Indian Government API endpoint
    url = "https://api.data.gov.in/resource/9ef84268-d588-465a-a308-a864a43d0070"
    
    # Parameters for the real API
    params = {
        "api-key": "579b464db66ec23bdd0000016c8c15c073f94fc1608add75486d243f",
        "format": "json",
        "limit": 10,  # Get first 10 records
        "offset": 0
    }
    
    try:
        print("🔍 Testing Real Indian Government API...")
        print(f"URL: {url}")
        print(f"Parameters: {params}")
        print("-" * 50)
        
        response = requests.get(url, params=params, timeout=30)
        
        print(f"Status Code: {response.status_code}")
        print(f"Response Headers: {dict(response.headers)}")
        print("-" * 50)
        
        if response.status_code == 200:
            data = response.json()
            print("✅ SUCCESS: Real Government API Response")
            print(f"Total Records: {data.get('total', 'N/A')}")
            print(f"Count: {data.get('count', 'N/A')}")
            print(f"Status: {data.get('status', 'N/A')}")
            print("-" * 50)
            
            # Show first few records
            records = data.get('records', [])
            print(f"First {min(3, len(records))} records from real API:")
            for i, record in enumerate(records[:3]):
                print(f"\nRecord {i+1}:")
                print(f"  Commodity: {record.get('commodity', 'N/A')}")
                print(f"  Variety: {record.get('variety', 'N/A')}")
                print(f"  Market: {record.get('market', 'N/A')}")
                print(f"  State: {record.get('state', 'N/A')}")
                print(f"  District: {record.get('district', 'N/A')}")
                print(f"  Min Price: {record.get('min_price', 'N/A')}")
                print(f"  Max Price: {record.get('max_price', 'N/A')}")
                print(f"  Modal Price: {record.get('modal_price', 'N/A')}")
                print(f"  Date: {record.get('date', 'N/A')}")
                print(f"  Arrival Quantity: {record.get('arrival_quantity', 'N/A')}")
                
        else:
            print(f"❌ ERROR: HTTP {response.status_code}")
            print(f"Response: {response.text}")
            
    except requests.exceptions.RequestException as e:
        print(f"❌ NETWORK ERROR: {e}")
    except json.JSONDecodeError as e:
        print(f"❌ JSON PARSE ERROR: {e}")
        print(f"Raw Response: {response.text}")
    except Exception as e:
        print(f"❌ UNEXPECTED ERROR: {e}")

def test_with_filters():
    """Test the API with commodity and state filters"""
    
    url = "https://api.data.gov.in/resource/9ef84268-d588-465a-a308-a864a43d0070"
    
    # Test with commodity filter
    params = {
        "api-key": "579b464db66ec23bdd0000016c8c15c073f94fc1608add75486d243f",
        "format": "json",
        "limit": 5,
        "offset": 0,
        "filters[commodity]": "Wheat"  # Filter by commodity
    }
    
    try:
        print("\n🔍 Testing with Wheat filter...")
        response = requests.get(url, params=params, timeout=30)
        
        if response.status_code == 200:
            data = response.json()
            records = data.get('records', [])
            print(f"✅ Found {len(records)} Wheat records")
            
            for record in records[:2]:  # Show first 2 wheat records
                print(f"  - {record.get('commodity')} ({record.get('variety')}) at {record.get('market')}, {record.get('state')}")
                print(f"    Price: ₹{record.get('modal_price')} per quintal")
        else:
            print(f"❌ Filter test failed: {response.status_code}")
            
    except Exception as e:
        print(f"❌ Filter test error: {e}")

if __name__ == "__main__":
    print("=" * 60)
    print("🇮🇳 INDIAN GOVERNMENT MANDI PRICES API TEST")
    print("=" * 60)
    
    test_real_government_api()
    test_with_filters()
    
    print("\n" + "=" * 60)
    print("📝 SUMMARY:")
    print("- Your Python Flask app returns MOCK data")
    print("- The Android app is configured for REAL government API")
    print("- The real API provides live mandi prices from across India")
    print("=" * 60) 