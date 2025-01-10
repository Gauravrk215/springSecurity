package com.realnet.Billing.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.realnet.Billing.Entitys.ApprovalHistory_t;
import com.realnet.Billing.Repositorys.ApprovalHistory_Repository;

@Service
public class ApprovalHistory_Service {

	@Autowired
	private ApprovalHistory_Repository Repository;

//	@Autowired
//	private ServiceOrder_Repository serviceOrder_Repository1;

	public ApprovalHistory_t Savedata(ApprovalHistory_t data) {
		return Repository.save(data);
	}

	public List<ApprovalHistory_t> getdetails() {
		return (List<ApprovalHistory_t>) Repository.findAll();
	}

	public ApprovalHistory_t getdetailsbyId(Long id) {
		return Repository.findById(id).get();
	}

	public void delete_by_id(Long id) {
		Repository.deleteById(id);
	}

	public ApprovalHistory_t update(ApprovalHistory_t data, Long id) {
		ApprovalHistory_t old = Repository.findById(id).get();
		old.setDocument_type(data.getDocument_type());
		old.setDocument_id(data.getDocument_id());
		old.setActioned_by(data.getActioned_by());
		old.setAction(data.getAction());
		old.setComments(data.getComments());
		final ApprovalHistory_t test = Repository.save(old);
		return test;
	}

//	 public ServiceOrder_t updateServiceOrderStatus(Long serviceOrderId, String newStatus, String actionedBy, String comments) {
//	        try {
//	            Optional<ServiceOrder_t> optionalServiceOrder = serviceOrder_Repository.findById(serviceOrderId);
//	            if (optionalServiceOrder.isEmpty()) {
//	                throw new EntityNotFoundException("Service Order not found with ID: " + serviceOrderId);
//	            }
//
//	            ServiceOrder_t serviceOrder = optionalServiceOrder.get();
//	            serviceOrder.setStatus(newStatus);
//	            serviceOrder_Repository.save(serviceOrder);
//
//	            // Insert data into Approval History table
//	            ApprovalHistory_t approvalHistory = new ApprovalHistory_t();
//	            approvalHistory.setDocument_type("ServiceOrder");
//	            approvalHistory.setDocument_id(serviceOrderId);
//	            approvalHistory.setActioned_by(actionedBy);
//	            approvalHistory.setAction("Status Updated");
//	            approvalHistory.setComments(comments);
//	            // Set other fields in the Approval History table as needed
//
//	            Repository.save(approvalHistory);
//
//	            return serviceOrder;
//	        } catch (EntityNotFoundException ex) {
//	            // Handle EntityNotFoundException (Service Order not found)
//	            // You can log the error, return a custom error response, or throw a new exception.
//	            throw ex;
//	        } catch (Exception ex) {
//	            // Handle other exceptions (e.g., database errors, unexpected issues)
//	            // You can log the error, return a custom error response, or throw a new exception.
//	            throw new RuntimeException("Error updating Service Order status.", ex);
//	        }
//	    }
//
//	public ServiceOrderDto updateServiceOrderStatus(Long serviceOrderId, String newStatus, String actionedBy, String comments) {
//        // ... existing code to update status ...
//		
//		
//
//        // Convert ServiceOrder_t entity to ServiceOrderDto
//        ServiceOrderDto serviceOrderDto = convertToDto(serviceOrder);
//
//        return serviceOrderDto;
//    }
//
//	public ServiceOrderDto updateServiceOrderStatus(Long serviceOrderId, String newStatus, String actionedBy,
//			String comments) {
//		try {
//			Optional<ServiceOrder_t> optionalServiceOrder = serviceOrder_Repository.findById(serviceOrderId);
//			if (!optionalServiceOrder.isPresent()) {
//				throw new EntityNotFoundException("Service Order not found with ID: " + serviceOrderId);
//			}
//
//			ServiceOrder_t serviceOrder = optionalServiceOrder.get();
//			serviceOrder.setStatus(newStatus);
//			serviceOrder_Repository.save(serviceOrder);
//
//			// Insert data into Approval History table
//			ApprovalHistory_t approvalHistory = new ApprovalHistory_t();
//			approvalHistory.setDocument_type("ServiceOrder");
//			approvalHistory.setDocument_id(serviceOrderId);
//			approvalHistory.setActioned_by(actionedBy);
//			approvalHistory.setAction("Status Updated");
//			approvalHistory.setComments(comments);
//			// Set other fields in the Approval History table as needed
//
//			Repository.save(approvalHistory);
//
//			// Convert ServiceOrder_t entity to DTO and return
//			return convertToDto(serviceOrder);
//		} catch (EntityNotFoundException ex) {
//			// Handle EntityNotFoundException (Service Order not found)
//			// You can log the error, return a custom error response, or throw a new
//			// exception.
//			throw ex;
//		} catch (Exception ex) {
//			// Handle other exceptions (e.g., database errors, unexpected issues)
//			// You can log the error, return a custom error response, or throw a new
//			// exception.
//			throw new RuntimeException("Error updating Service Order status.", ex);
//		}
//	}

//	private ServiceOrderDto convertToDto(ServiceOrder_t serviceOrder) {
//		// Perform mapping from ServiceOrder_t entity to ServiceOrderDto
//		ModelMapper modelMapper = new ModelMapper();
//		return modelMapper.map(serviceOrder, ServiceOrderDto.class);
//	}
}