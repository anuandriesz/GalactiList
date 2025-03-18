package com.example.galactilist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.galactilist.data.PlanetRepository
import com.example.galactilist.network.responses.PlanetsResponse
import com.example.galactilist.ui.planetList.PlanetListViewModel
import com.example.galactilist.utils.Resource
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.mockito.kotlin.argumentCaptor
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class PlanetListViewModelTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var planetRepository: PlanetRepository

    @Mock
    private lateinit var observer: Observer<Resource<PlanetsResponse?>>

    private lateinit var viewModel: PlanetListViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher) // Set test dispatcher
        viewModel = PlanetListViewModel(planetRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // Reset dispatcher after tests
    }

    @Test
    fun `perform get planet list success`() = runTest {
        // Arrange
        val response = PlanetsResponse(1, null, null, emptyList())

        // Ensure repository returns a single Resource<PlanetsResponse?>
        `when`(planetRepository.getPlanetList()).thenReturn(Resource.Success(response))

        // Observe LiveData
        viewModel.planetList.observeForever(observer)

        // Act
        viewModel.performGetPlanets()
        advanceUntilIdle() // Ensure coroutines complete

        // Assert
        verify(observer).onChanged(Resource.Success(response))
        assertTrue(viewModel.planetList.value is Resource.Success)

        // Cleanup
        viewModel.planetList.removeObserver(observer)
    }

    @Test
    fun `perform get planet list error`() = runTest {
        // Arrange
        val errorMessage = "Error"
        val error = Exception(errorMessage) // Ensure Exception has a message

        // Mock repository to return an Error state
        `when`(planetRepository.getPlanetList()).thenReturn(Resource.Error(error))

        // Observe LiveData
        viewModel.planetList.observeForever(observer)

        // Act
        viewModel.performGetPlanets()
        advanceUntilIdle() // Ensure coroutines complete

        // Assert
        val argumentCaptor = argumentCaptor<Resource<PlanetsResponse?>>()

        // Verify LiveData emitted Loading first
        verify(observer).onChanged(Resource.Loading)

        // Capture the emitted Error state
        verify(observer, times(2)).onChanged(argumentCaptor.capture())

        val capturedValues = argumentCaptor.allValues
        assertTrue(capturedValues[0] is Resource.Loading)
        assertTrue(capturedValues[1] is Resource.Error)

        val capturedError = capturedValues[1] as Resource.Error
        assertTrue(capturedError.exception is Exception)

        assertTrue(viewModel.planetList.value is Resource.Error)

        // Cleanup
        viewModel.planetList.removeObserver(observer)
    }
}