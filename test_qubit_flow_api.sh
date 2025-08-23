#!/bin/bash

# Qubit Flow API Test Script
# This script tests all the REST API endpoints for Qubit Flow App

# Configuration
BASE_URL="http://localhost:8080/api/quantum"
CONTENT_TYPE="Content-Type: application/json"
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

print_section() {
    echo -e "\n${BLUE}========================================${NC}"
    echo -e "${BLUE}$1${NC}"
    echo -e "${BLUE}========================================${NC}\n"
}

print_test() {
    echo -e "${YELLOW}Testing: $1${NC}"
}

print_success() {
    echo -e "${GREEN}✓ SUCCESS: $1${NC}"
}

print_error() {
    echo -e "${RED}✗ ERROR: $1${NC}"
}

test_endpoint() {
    local method=$1
    local endpoint=$2
    local description=$3
    local data=$4
    
    print_test "$description"
    
    if [ "$method" = "GET" ]; then
        response=$(curl -s -w "\n%{http_code}" -X GET "$BASE_URL$endpoint")
    elif [ "$method" = "POST" ]; then
        if [ -z "$data" ]; then
            response=$(curl -s -w "\n%{http_code}" -X POST "$BASE_URL$endpoint")
        else
            response=$(curl -s -w "\n%{http_code}" -X POST -H "$CONTENT_TYPE" -d "$data" "$BASE_URL$endpoint")
        fi
    elif [ "$method" = "DELETE" ]; then
        response=$(curl -s -w "\n%{http_code}" -X DELETE "$BASE_URL$endpoint")
    fi
    
    http_code=$(echo "$response" | tail -n1)
    body=$(echo "$response" | head -n -1)
    
    if [ "$http_code" -eq 200 ] || [ "$http_code" -eq 201 ]; then
        print_success "$description (HTTP $http_code)"
        echo "Response: $(echo "$body" | jq . 2>/dev/null || echo "$body")"
    else
        print_error "$description (HTTP $http_code)"
        echo "Response: $body"
    fi
    
    echo ""
    sleep 1
}

# Check if server is running
echo -e "${BLUE}Checking if server is running...${NC}"
if ! curl -s "$BASE_URL/circuit/info" > /dev/null; then
    echo -e "${RED}Server is not running at $BASE_URL${NC}"
    echo -e "${YELLOW}Please start the Spring Boot application first${NC}"
    exit 1
fi
print_success "Server is running"

# Start testing
print_section "QUANTUM CIRCUIT API TESTS"

# 1. Circuit Management Tests
print_section "1. CIRCUIT MANAGEMENT"
test_endpoint "POST" "/circuit/create?qubits=3" "Create new circuit with 3 qubits"
test_endpoint "GET" "/circuit/info" "Get circuit information"
test_endpoint "GET" "/circuit/current" "Get current circuit state"
test_endpoint "GET" "/circuit/stats" "Get circuit statistics"

# 2. Qubit Management Tests
print_section "2. QUBIT MANAGEMENT"
test_endpoint "GET" "/qubits/count" "Get current qubit count"
test_endpoint "GET" "/qubits/info" "Get detailed qubit information"
test_endpoint "POST" "/qubits/add" "Add a qubit"
test_endpoint "DELETE" "/qubits/remove" "Remove a qubit"
test_endpoint "POST" "/qubits/set-count?count=4" "Set qubit count to 4"

# 3. Gate Operations Tests
print_section "3. GATE OPERATIONS"
test_endpoint "POST" "/gates/hadamard?qubit=0" "Add Hadamard gate to qubit 0"
test_endpoint "POST" "/gates/pauli-x?qubit=1" "Add Pauli-X gate to qubit 1"
test_endpoint "POST" "/gates/cnot?control=0&target=2" "Add CNOT gate (control=0, target=2)"

# 4. State Management Tests
print_section "4. STATE MANAGEMENT"
test_endpoint "GET" "/state/current" "Get current quantum state"
test_endpoint "GET" "/state/probabilities" "Get measurement probabilities"
test_endpoint "POST" "/state/reset" "Reset state to |0...0⟩"

# 5. Simulation Tests
print_section "5. SIMULATION"
test_endpoint "POST" "/simulate/execute" "Execute circuit"
test_endpoint "POST" "/simulate/run" "Run full simulation"

# 6. Measurement Tests
print_section "6. MEASUREMENT"
test_endpoint "POST" "/measure/add?qubit=0" "Add measurement to qubit 0"
test_endpoint "POST" "/measure/add-all" "Add measurements to all qubits"
test_endpoint "POST" "/measure/qubit?qubit=0" "Measure specific qubit"
test_endpoint "POST" "/measure/all" "Measure all qubits"
test_endpoint "GET" "/measure/results" "Get measurement results"

# 7. History Tests
print_section "7. COMMAND HISTORY"
test_endpoint "GET" "/history/status" "Get history status"
test_endpoint "POST" "/history/undo" "Undo last operation"
test_endpoint "POST" "/history/redo" "Redo last operation"

# 8. Additional Circuit Operations
print_section "8. ADDITIONAL CIRCUIT OPERATIONS"
test_endpoint "POST" "/circuit/clear" "Clear circuit"

# 9. Cleanup Tests
print_section "9. CLEANUP"
test_endpoint "DELETE" "/measure/results" "Clear measurement results"

# 10. Error Handling Tests
print_section "10. ERROR HANDLING TESTS"
test_endpoint "POST" "/gates/hadamard?qubit=99" "Add Hadamard to invalid qubit (should fail)"
test_endpoint "POST" "/gates/cnot?control=0&target=0" "Add CNOT with same control/target (should fail)"
test_endpoint "POST" "/circuit/create?qubits=-1" "Create circuit with negative qubits (should fail)"

# Final summary
print_section "TEST SUMMARY"
echo -e "${GREEN}API testing completed!${NC}"
echo -e "${YELLOW}Check the responses above for any errors.${NC}"
echo -e "${BLUE}All endpoints have been tested for basic functionality.${NC}"

# Performance test (optional)
echo -e "\n${YELLOW}Would you like to run a performance test? (y/n)${NC}"
read -r response
if [ "$response" = "y" ] || [ "$response" = "Y" ]; then
    print_section "PERFORMANCE TEST"
    echo -e "${YELLOW}Running 10 rapid circuit operations...${NC}"
    
    for i in {1..10}; do
        curl -s -X POST "$BASE_URL/circuit/create?qubits=2" > /dev/null
        curl -s -X POST "$BASE_URL/gates/hadamard?qubit=0" > /dev/null
        curl -s -X POST "$BASE_URL/gates/cnot?control=0&target=1" > /dev/null
        curl -s -X POST "$BASE_URL/simulate/execute" > /dev/null
        echo -n "."
    done
    
    echo ""
    print_success "Performance test completed - 40 operations executed"
fi

echo -e "\n${GREEN}All tests finished!${NC}"